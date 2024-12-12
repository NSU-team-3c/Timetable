package ru.nsu.timetable.services;

import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import ru.nsu.timetable.models.dto.*;

import javax.xml.parsers.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public TimetableDTO parseTimetable(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream inputStream = new FileInputStream(xmlFile);
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            InputSource inputSource = new InputSource(reader);
            inputSource.setEncoding("UTF-8");

            Document doc = builder.parse(inputSource);
            doc.getDocumentElement().normalize();

            List<EventDTO> events = new ArrayList<>();
            NodeList dayNodes = doc.getElementsByTagName("day");

            for (int i = 0; i < dayNodes.getLength(); i++) {
                Element dayElement = (Element) dayNodes.item(i);
                int dayNumber = Integer.parseInt(dayElement.getAttribute("number"));
                events.addAll(parseEvents(dayElement, dayNumber));
            }

            return new TimetableDTO(123L, events);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse XML file", e);
        }
    }

    private List<EventDTO> parseEvents(Element dayElement, int dayNumber) {
        List<EventDTO> events = new ArrayList<>();

        NodeList timeSlotNodes = dayElement.getElementsByTagName("timeSlot");
        for (int j = 0; j < timeSlotNodes.getLength(); j++) {
            Element timeSlotElement = (Element) timeSlotNodes.item(j);

            Long id = Long.parseLong(getElementTextContent(timeSlotElement, "id"));
            String teacherName = getElementAttributeValue(timeSlotElement, "teacher");
            String subjectName = getElementAttributeValue(timeSlotElement, "subject");
            String roomName = getElementAttributeValue(timeSlotElement, "room");

            Date[] startEndTimes = calculateTimes(dayNumber, id.intValue());

            events.add(new EventDTO(id, startEndTimes[0], startEndTimes[1], teacherName, subjectName, roomName));
        }

        return events;
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

    private String getElementAttributeValue(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            Element element = (Element) nodes.item(0);
            return element.getAttribute("name");
        }
        return "";
    }

    private String getElementTextContent(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        return (list.getLength() > 0) ? list.item(0).getTextContent() : "";
    }
}
