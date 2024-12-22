package ru.nsu.timetable.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nsu.timetable.exceptions.BadRequestException;
import ru.nsu.timetable.exceptions.ParsingException;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.entities.*;
import ru.nsu.timetable.repositories.*;

import javax.xml.parsers.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class XmlParserService {

    private static final String BASE_DATE = "2024-12-16";
    private static final Map<Integer, String> PAIR_TIMES = Map.of(
            1, "09:00-10:35",
            2, "10:50-12:25",
            3, "12:40-14:15",
            4, "14:30-16:05",
            5, "16:20-17:55",
            6, "18:10-19:45",
            7, "20:00-21:35"
    );

    private final TimetableRepository timetableRepository;

    private final EventRepository eventRepository;

    private final SubjectRepository subjectRepository;

    private final RoomRepository roomRepository;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    public Timetable parseTimetable(String filePath) {
        try {
            System.out.println("Starting to parse timetable from file: " + filePath);

            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream inputStream = new FileInputStream(xmlFile);
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            InputSource inputSource = new InputSource(reader);
            inputSource.setEncoding("UTF-8");

            Document doc = builder.parse(inputSource);
            doc.getDocumentElement().normalize();

            System.out.println("XML file parsed successfully. Normalizing document...");

            List<Event> events = new ArrayList<>();
            NodeList dayNodes = doc.getElementsByTagName("day");

            System.out.println("Found " + dayNodes.getLength() + " days in the timetable.");

            for (int i = 0; i < dayNodes.getLength(); i++) {
                Element dayElement = (Element) dayNodes.item(i);
                int dayNumber = Integer.parseInt(dayElement.getAttribute("number"));
                System.out.println("Parsing events for day " + dayNumber);

                events.addAll(parseEvents(dayElement, dayNumber));
            }

            Timetable timetable = Timetable.builder()
                    .id(1L)
                    .events(events)
                    .build();

            System.out.println("Saving timetable with " + events.size() + " events to the database.");

            timetable = timetableRepository.save(timetable);

            return timetable;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParsingException("Failed to parse XML file", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while processing timetable", e);
        }
    }

    private List<Event> parseEvents(Element dayElement, int dayNumber) {
        List<Event> events = new ArrayList<>();

        NodeList timeSlotNodes = dayElement.getElementsByTagName("timeSlots");
        for (int j = 0; j < timeSlotNodes.getLength(); j++) {
            Element timeSlotElement = (Element) timeSlotNodes.item(j);

            long originalId = Long.parseLong(getElementTextContent(timeSlotElement, "id"));

            String groupId = getElementAttribute(timeSlotElement, "group", "id");
            String subjectId = getElementAttribute(timeSlotElement, "subject", "id");
            String teacherId = getElementAttribute(timeSlotElement, "teacher", "id");
            String roomId = getElementAttribute(timeSlotElement, "room", "id");

            System.out.println("Processing time slot: " +
                    "Group ID = " + groupId +
                    ", Subject ID = " + subjectId +
                    ", Teacher ID = " + teacherId +
                    ", Room ID = " + roomId);

            if (groupId.isEmpty() || subjectId.isEmpty() || teacherId.isEmpty() || roomId.isEmpty()) {
                System.err.println("Empty field detected: Group ID = " + groupId + ", Subject ID = " + subjectId + ", Teacher ID = " + teacherId + ", Room ID = " + roomId);
                throw new BadRequestException("One or more required fields are missing: " + groupId + ", " + subjectId + ", " + teacherId + ", " + roomId);
            }

            List<Group> groups = new ArrayList<>();
            Group group = groupRepository.findById(Long.parseLong(groupId))
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + groupId));
            groups.add(group);

            Subject subject = subjectRepository.findById(Long.parseLong(subjectId))
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));
            User teacher = userRepository.findById(Long.parseLong(teacherId))
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + teacherId));
            Room room = roomRepository.findById(Long.parseLong(roomId))
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + roomId));

            Date[] startEndTimes = calculateTimes(dayNumber, (int) originalId);

            Event event = Event.builder()
                    .startTime(startEndTimes[0])
                    .endTime(startEndTimes[1])
                    .groups(groups)
                    .subject(subject)
                    .teacher(teacher)
                    .room(room)
                    .build();

            events.add(event);
        }

        return eventRepository.saveAll(events);
    }

    private String getElementAttribute(Element parent, String tagName, String attributeName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Element element = (Element) nodeList.item(0);
            return element.getAttribute(attributeName);
        }
        return "";
    }

    private Long parseLongSafe(String str) {
        if (str == null || str.trim().isEmpty()) {
            System.err.println("Empty or null value provided for ID: " + str);
            return null;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + str);
            return null;
        }
    }

    private Date[] calculateTimes(int dayNumber, int pairNumber) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            TimeZone timeZone = TimeZone.getTimeZone("Asia/Novosibirsk");
            dateFormat.setTimeZone(timeZone);

            Calendar calendar = Calendar.getInstance(timeZone);
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(BASE_DATE));

            calendar.add(Calendar.DAY_OF_YEAR, dayNumber - 1);

            String[] timeRange = PAIR_TIMES.get(pairNumber).split("-");
            String startTime = timeRange[0];
            String endTime = timeRange[1];

            Date startDate = dateFormat.parse(
                    new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()) + " " + startTime
            );
            Date endDate = dateFormat.parse(
                    new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()) + " " + endTime
            );

            return new Date[]{startDate, endDate};
        } catch (Exception e) {
            throw new RuntimeException("Error calculating event times", e);
        }
    }

    private String getElementTextContent(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        return (list.getLength() > 0) ? list.item(0).getTextContent() : "";
    }
}
