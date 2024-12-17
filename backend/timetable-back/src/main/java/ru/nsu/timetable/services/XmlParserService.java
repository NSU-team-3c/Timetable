package ru.nsu.timetable.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
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

    private static final String BASE_DATE = "2024-04-29";
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

            Timetable timetable = new Timetable();
            timetable.setEvents(events);

            System.out.println("Saving timetable with " + events.size() + " events to the database.");

            //timetable = timetableRepository.save(timetable);

            return timetable;
        } catch (Exception e) {
            System.err.println("Error while parsing timetable XML file: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to parse XML file", e);
        }
    }

    private List<Event> parseEvents(Element dayElement, int dayNumber) {
        List<Event> events = new ArrayList<>();

        NodeList timeSlotNodes = dayElement.getElementsByTagName("timeSlot");
        for (int j = 0; j < timeSlotNodes.getLength(); j++) {
            Element timeSlotElement = (Element) timeSlotNodes.item(j);

            Long id = Long.parseLong(getElementTextContent(timeSlotElement, "id"));

            String teacherId = timeSlotElement.getElementsByTagName("teacher").item(0).getAttributes().getNamedItem("id").getTextContent();
            String subjectId = timeSlotElement.getElementsByTagName("subject").item(0).getAttributes().getNamedItem("id").getTextContent();
            String roomId = timeSlotElement.getElementsByTagName("room").item(0).getAttributes().getNamedItem("id").getTextContent();
            System.out.println("Processing time slot: " +
                    "Teacher ID = " + teacherId +
                    ", Subject ID = " + subjectId +
                    ", Room ID = " + roomId);

            if (teacherId.isEmpty() || subjectId.isEmpty() || roomId.isEmpty()) {
                System.err.println("Empty field detected: Teacher ID = " + teacherId + ", Subject ID = " + subjectId + ", Room ID = " + roomId);
                continue;
            }
            NodeList groupNodes = timeSlotElement.getElementsByTagName("group");
            List<Group> groups = new ArrayList<>();
            for (int k = 0; k < groupNodes.getLength(); k++) {
                Element groupElement = (Element) groupNodes.item(k);
                String groupId = groupElement.getAttribute("id");

                Long groupIdLong = Long.parseLong(groupId);

                Group group = groupRepository.findById(groupIdLong)
                        .orElseThrow(() -> new RuntimeException("Group not found: " + groupId));
                groups.add(group);
            }

            Date[] startEndTimes = calculateTimes(dayNumber, id.intValue());

            if (teacherId == null || teacherId.trim().isEmpty()) {
                throw new RuntimeException("Teacher ID is empty in XML");
            }
            if (subjectId == null || subjectId.trim().isEmpty()) {
                throw new RuntimeException("Subject ID is empty in XML");
            }
            if (roomId == null || roomId.trim().isEmpty()) {
                throw new RuntimeException("Room ID is empty in XML");
            }

            User teacher = userRepository.findById(parseLongSafe(teacherId))
                    .orElseThrow(() -> new RuntimeException("Teacher not found: " + teacherId));
            Subject subject = subjectRepository.findById(parseLongSafe(subjectId))
                    .orElseThrow(() -> new RuntimeException("Subject not found: " + subjectId));
            Room room = roomRepository.findById(parseLongSafe(roomId))
                    .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));

            Event event = new Event();
            event.setId(id);
            event.setStartTime(startEndTimes[0]);
            event.setEndTime(startEndTimes[1]);
            event.setTeacher(teacher);
            event.setSubject(subject);
            event.setRoom(room);
            event.setGroups(groups);

            events.add(event);
        }

        return eventRepository.saveAll(events);
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
            Calendar calendar = Calendar.getInstance();
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
