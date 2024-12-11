package ru.nsu.timetable.services;

import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import ru.nsu.timetable.models.dto.DayDTO;
import ru.nsu.timetable.models.dto.GroupDTO;
import ru.nsu.timetable.models.dto.TimeSlotDTO;
import ru.nsu.timetable.models.dto.TimetableDTO;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

@Service
public class XmlParserService {

    //sorry
    /*private static final Map<String, Long> ID_MAPPING = Map.ofEntries(
            Map.entry("teamPr", 101L),
            Map.entry("prog", 102L),
            Map.entry("math", 103L),
            Map.entry("mobDev", 104L),
            Map.entry("t1", 201L),
            Map.entry("t2", 202L),
            Map.entry("t3", 203L),
            Map.entry("t4", 204L),
            Map.entry("r1", 301L),
            Map.entry("r2", 302L),
            Map.entry("r3", 303L),
            Map.entry("r4", 304L)
    );

    public TimetableDTO parseTimetable(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Map<Integer, DayDTO> days = new HashMap<>();

            NodeList dayNodes = doc.getElementsByTagName("day");
            for (int i = 0; i < dayNodes.getLength(); i++) {
                Element dayElement = (Element) dayNodes.item(i);
                int dayNumber = Integer.parseInt(dayElement.getAttribute("number"));
                String dayName = dayElement.getAttribute("name");

                List<TimeSlotDTO> timeSlots = parseTimeSlots(dayElement);
                days.put(dayNumber, new DayDTO(dayNumber, dayName, new HashSet<>(timeSlots)));
            }

            Set<Long> facultyIds = new HashSet<>();
            Set<Long> timeSlotIds = new HashSet<>();
            for (DayDTO day : days.values()) {
                for (TimeSlotDTO timeSlot : day.timeSlots()) {
                    timeSlotIds.add(timeSlot.id());
                    facultyIds.add(timeSlot.teacherId());
                }
            }

            return new TimetableDTO(123L, facultyIds, timeSlotIds);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse XML file", e);
        }
    }

    private List<TimeSlotDTO> parseTimeSlots(Element dayElement) {
        List<TimeSlotDTO> timeSlotDTOs = new ArrayList<>();

        NodeList timeSlotNodes = dayElement.getElementsByTagName("timeSlot");
        for (int j = 0; j < timeSlotNodes.getLength(); j++) {
            Element timeSlotElement = (Element) timeSlotNodes.item(j);

            Long timeSlotId = Long.parseLong(getElementTextContent(timeSlotElement, "id"));
            Long subjectId = mapToLong(timeSlotElement, "subject", "id");
            Long teacherId = mapToLong(timeSlotElement, "teacher", "id");
            Long roomId = mapToLong(timeSlotElement, "room", "id");

            Set<GroupDTO> groups = parseGroups(timeSlotElement);

            timeSlotDTOs.add(new TimeSlotDTO(timeSlotId, null, null, subjectId, roomId, teacherId));
        }

        return timeSlotDTOs;
    }

    private Long mapToLong(Element parent, String tagName, String attribute) {
        Element element = (Element) parent.getElementsByTagName(tagName).item(0);
        if (element != null) {
            String id = element.getAttribute(attribute);
            return ID_MAPPING.getOrDefault(id, -1L);
        }
        return -1L;
    }

    private Set<GroupDTO> parseGroups(Element timeSlotElement) {
        Set<GroupDTO> groups = new HashSet<>();
        NodeList groupNodes = timeSlotElement.getElementsByTagName("group");
        for (int k = 0; k < groupNodes.getLength(); k++) {
            Element groupElement = (Element) groupNodes.item(k);
            Long groupId = Long.parseLong(groupElement.getAttribute("id"));
            String groupName = groupElement.getAttribute("name");
            groups.add(new GroupDTO(groupId, groupName, new HashSet<>()));
        }
        return groups;
    }

    private String getElementTextContent(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        return (list.getLength() > 0) ? list.item(0).getTextContent() : "";
    }*/
}
