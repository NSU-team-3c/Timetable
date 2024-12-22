package ru.nsu.timetable.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.XmlGenerationException;
import ru.nsu.timetable.models.entities.*;
import ru.nsu.timetable.models.entities.TimeSlot;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;

@Service
public class RequirementsXmlGeneratorService {

    @XmlRootElement(name = "requirements")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class Requirements {
        @XmlElement(name = "global")
        private Global global;

        @XmlElement(name = "group")
        private List<GroupRequirement> groups = new ArrayList<>();

        @XmlElement(name = "room")
        private List<RoomXml> rooms = new ArrayList<>();

        @XmlElement(name = "availableTeacherSlots")
        private List<TeacherAvailableSlots> availableTeacherSlots = new ArrayList<>();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class Global {
        @XmlAttribute(name = "slotsperweek")
        private int slotsPerWeek;

        @XmlAttribute(name = "slotsperday")
        private int slotsPerDay;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class GroupRequirement {
        @XmlAttribute(name = "id")
        private long id;

        @XmlAttribute(name = "amount")
        private int amount;

        @XmlElement(name = "req")
        private List<Requirement> requirements = new ArrayList<>();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class Requirement {
        @XmlAttribute(name = "subject")
        private long subjectId;

        @XmlAttribute(name = "teacher")
        private long teacherId;

        @XmlAttribute(name = "amount")
        private int amount;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RoomXml {
        @XmlAttribute(name = "id")
        private long id;

        @XmlAttribute(name = "capacity")
        private int capacity;

        @XmlElement(name = "allocate")
        private List<RoomAllocate> allocations = new ArrayList<>();
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RoomAllocate {
        @XmlAttribute(name = "day")
        private int day;

        @XmlAttribute(name = "start")
        private int start;

        @XmlAttribute(name = "end")
        private int end;

        public RoomAllocate(int day, int start, int end) {
            this.day = day;
            this.start = start;
            this.end = end;
        }
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TeacherAvailableSlots {
        @XmlAttribute(name = "teacher")
        private long teacherId;

        @XmlElement(name = "slot")
        private List<XmlTimeSlot> slots = new ArrayList<>();
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class XmlTimeSlot {
        @XmlAttribute(name = "day")
        private int day;

        @XmlAttribute(name = "start")
        private int start;

        @XmlAttribute(name = "end")
        private int end;
    }

    public void generateXml(List<Group> groups, List<Room> rooms, List<User> teachers,
                            int slotsPerWeek, int slotsPerDay, String filePath) {
        try {
            Requirements requirements = new Requirements();

            Global global = new Global();
            global.setSlotsPerWeek(slotsPerWeek);
            global.setSlotsPerDay(slotsPerDay);
            requirements.setGlobal(global);

            for (Group group : groups) {
                GroupRequirement groupReq = new GroupRequirement();
                groupReq.setId(group.getId());
                groupReq.setAmount(group.getCapacity());

                List<Requirement> reqList = new ArrayList<>();
                for (Subject subject : group.getSubjects()) {
                    for (User teacher : subject.getTeachers()) {
                        Requirement req = new Requirement();
                        req.setSubjectId(subject.getId());
                        req.setTeacherId(teacher.getId());

                        int semesterDuration = subject.getDuration();
                        int weeksInSemester = 15;

                        int weeklyAmount = semesterDuration / weeksInSemester;

                        req.setAmount(weeklyAmount);
                        reqList.add(req);
                    }
                }
                groupReq.setRequirements(reqList);
                requirements.getGroups().add(groupReq);
            }

            for (Room room : rooms) {
                RoomXml roomElement = new RoomXml();
                roomElement.setId(room.getId());
                roomElement.setCapacity(room.getCapacity());

                List<RoomAllocate> allocations = new ArrayList<>();
                allocations.add(new RoomAllocate(1, 1, 7));
                allocations.add(new RoomAllocate(2, 1, 7));
                allocations.add(new RoomAllocate(3, 1, 7));
                allocations.add(new RoomAllocate(4, 1, 7));
                allocations.add(new RoomAllocate(5, 1, 7));
                allocations.add(new RoomAllocate(6, 1, 7));
                allocations.add(new RoomAllocate(7, 1, 7));
                roomElement.setAllocations(allocations);

                requirements.getRooms().add(roomElement);
            }

            for (User teacher : teachers) {
                TeacherAvailableSlots availableSlots = new TeacherAvailableSlots();
                availableSlots.setTeacherId(teacher.getId());

                Map<Integer, List<XmlTimeSlot>> slotsByDay = new HashMap<>();

                for (TimeSlot timeSlot : teacher.getAvailableTimeSlots()) {
                    int dayOfWeek = getDayOfWeek(timeSlot.getStartTime());

                    int pairNumber = getPairNumber(timeSlot);

                    if (pairNumber != -1) {
                        XmlTimeSlot xmlSlot = new XmlTimeSlot();
                        xmlSlot.setDay(dayOfWeek);
                        xmlSlot.setStart(pairNumber);
                        xmlSlot.setEnd(pairNumber);

                        slotsByDay.computeIfAbsent(dayOfWeek, k -> new ArrayList<>()).add(xmlSlot);
                    }
                }

                for (List<XmlTimeSlot> slots : slotsByDay.values()) {
                    if (!slots.isEmpty()) {
                        List<XmlTimeSlot> mergedSlots = mergeConsecutiveSlots(slots);
                        availableSlots.getSlots().addAll(mergedSlots);
                    }
                }

                if (!availableSlots.getSlots().isEmpty()) {
                    requirements.getAvailableTeacherSlots().add(availableSlots);
                }
            }

            JAXBContext context = JAXBContext.newInstance(Requirements.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(requirements, new File(filePath));

            System.out.println("XML file successfully generated: " + filePath);
        } catch (JAXBException e) {
            throw new XmlGenerationException("Error while generating XML: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new XmlGenerationException("Unexpected error occurred while generating XML", e);
        }
    }

    private int getPairNumber(TimeSlot timeSlot) {
        Date startTime = timeSlot.getStartTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if (hour == 9 && minute >= 0 && minute < 40) {
            return 1;
        } else if (hour == 10 && minute >= 50 && minute < 55) {
            return 2;
        } else if (hour == 12 && minute >= 40 && minute < 55) {
            return 3;
        } else if (hour == 14 && minute >= 30 && minute < 55) {
            return 4;
        } else if (hour == 16 && minute >= 20 && minute < 55) {
            return 5;
        } else if (hour == 18 && minute >= 10 && minute < 45) {
            return 6;
        } else if (hour == 20 && minute >= 0 && minute < 35) {
            return 7;
        }
        return -1;
    }

    private List<XmlTimeSlot> mergeConsecutiveSlots(List<XmlTimeSlot> slots) {
        List<XmlTimeSlot> mergedSlots = new ArrayList<>();
        XmlTimeSlot currentSlot = null;

        for (XmlTimeSlot slot : slots) {
            if (currentSlot == null) {
                currentSlot = slot;
            } else if (currentSlot.getEnd() + 1 == slot.getStart()) {
                currentSlot.setEnd(slot.getEnd());
            } else {
                mergedSlots.add(currentSlot);
                currentSlot = slot;
            }
        }
        if (currentSlot != null) {
            mergedSlots.add(currentSlot);
        }
        return mergedSlots;
    }

    private int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SUNDAY) {
            return 7;
        } else {
            return dayOfWeek - 1;
        }
    }
}
