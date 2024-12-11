package ru.nsu.timetable.services;

import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import ru.nsu.timetable.models.dto.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.util.*;

@Service
public class RequirementsXmlGeneratorService {

    /*public void generateXml(List<GroupDTO> groups,
                            List<RoomDTO> rooms,
                            List<TeacherDTO> teachers,
                            Map<Long, List<RequirementDTO>> groupRequirements,
                            Map<Long, Set<Integer>> groupFreeSlots,
                            Map<Long, List<CouplingDTO>> groupCouplings,
                            Map<Long, List<AllocateDTO>> roomAllocations,
                            int slotsPerWeek, int slotsPerDay, String filePath)
            throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element rootElement = document.createElement("requirements");
        document.appendChild(rootElement);

        Element globalElement = document.createElement("global");
        globalElement.setAttribute("slotsperweek", String.valueOf(slotsPerWeek));
        globalElement.setAttribute("slotsperday", String.valueOf(slotsPerDay));
        rootElement.appendChild(globalElement);

        for (GroupDTO group : groups) {
            Element groupElement = document.createElement("group");
            groupElement.setAttribute("id", String.valueOf(group.id()));

            List<RequirementDTO> reqs = groupRequirements.getOrDefault(group.id(), List.of());
            for (RequirementDTO req : reqs) {
                Element reqElement = document.createElement("req");
                reqElement.setAttribute("subject", req.subject());
                reqElement.setAttribute("teacher", req.teacher());
                reqElement.setAttribute("amount", String.valueOf(req.amount()));
                groupElement.appendChild(reqElement);
            }

            List<CouplingDTO> couplings = groupCouplings.getOrDefault(group.id(), List.of());
            for (CouplingDTO coupling : couplings) {
                Element couplingElement = document.createElement("coupling");
                couplingElement.setAttribute("subject", coupling.subject());
                couplingElement.setAttribute("lesson1", String.valueOf(coupling.lesson1()));
                couplingElement.setAttribute("lesson2", String.valueOf(coupling.lesson2()));
                groupElement.appendChild(couplingElement);
            }

            Set<Integer> freeSlots = groupFreeSlots.getOrDefault(group.id(), Set.of());
            for (Integer slot : freeSlots) {
                Element freeElement = document.createElement("free");
                freeElement.setAttribute("slot", String.valueOf(slot));
                groupElement.appendChild(freeElement);
            }

            rootElement.appendChild(groupElement);
        }

        for (RoomDTO room : rooms) {
            Element roomElement = document.createElement("room");
            roomElement.setAttribute("id", "r" + room.id());

            List<AllocateDTO> allocations = roomAllocations.getOrDefault(room.id(), List.of());
            for (AllocateDTO allocation : allocations) {
                Element allocateElement = document.createElement("allocate");
                allocateElement.setAttribute("group", String.valueOf(allocation.groupId()));
                allocateElement.setAttribute("subject", allocation.subject());
                allocateElement.setAttribute("lesson", String.valueOf(allocation.lesson()));
                roomElement.appendChild(allocateElement);
            }

            rootElement.appendChild(roomElement);
        }

        for (TeacherDTO teacher : teachers) {
            if (teacher.freeDays() != null) {
                for (Integer day : teacher.freeDays()) {
                    Element freeDayElement = document.createElement("freeday");
                    freeDayElement.setAttribute("teacher", teacher.name());
                    freeDayElement.setAttribute("day", String.valueOf(day));
                    rootElement.appendChild(freeDayElement);
                }
            }
        }

        saveToFile(document, filePath);
    }

    private void saveToFile(Document document, String filePath) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);

    }*/
}
