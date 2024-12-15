package ru.nsu.timetable.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.models.entities.*;
import ru.nsu.timetable.models.entities.TimeSlot;
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

        @XmlElementWrapper(name = "groups")
        @XmlElement(name = "group")
        private List<GroupRequirement> groups = new ArrayList<>();

        @XmlElementWrapper(name = "rooms")
        @XmlElement(name = "room")
        private List<RoomXml> rooms = new ArrayList<>();

//        @XmlElementWrapper(name = "freeday")
//        @XmlElement(name = "teacher")
//        private List<TeacherFreeDay> freeDays = new ArrayList<>();

        @XmlElementWrapper(name = "availableTeacherSlots")
        @XmlElement(name = "day")
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

        @XmlElement(name = "req")
        private List<Requirement> requirements = new ArrayList<>();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class Requirement {
        @XmlAttribute(name = "subject")
        private String subject;

        @XmlAttribute(name = "teacher")
        private long teacherId;

        @XmlAttribute(name = "amount")
        private int amount;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RoomXml {
        @XmlAttribute(name = "id")
        private String id;

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

//    @Data
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public static class TeacherFreeDay {
//        @XmlAttribute(name = "teacher")
//        private long teacherId;
//
//        @XmlAttribute(name = "day")
//        private int day;
//    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TeacherAvailableSlots {
        @XmlAttribute(name = "teacher")
        private long teacherId;

        @XmlElementWrapper(name = "availableSlots")
        @XmlElement(name = "day")
        private List<DaySlots> availableSlots = new ArrayList<>();
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DaySlots {
        @XmlAttribute(name = "number")
        private int dayNumber;

        @XmlElement(name = "slot")
        private List<XmlTimeSlot> timeSlots = new ArrayList<>();
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class XmlTimeSlot {
        @XmlAttribute(name = "start")
        private int start;

        @XmlAttribute(name = "end")
        private int end;
    }

    public void generateXml(List<Group> groups, List<Room> rooms, List<Teacher> teachers,
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

                List<Requirement> reqList = new ArrayList<>();
                for (Subject subject : group.getSubjects()) {
                    for (Teacher teacher : subject.getTeachers()) {
                        Requirement req = new Requirement();
                        req.setSubject(subject.getName());
                        req.setTeacherId(teacher.getId());
                        req.setAmount(2);
                        reqList.add(req);
                    }
                }
                groupReq.setRequirements(reqList);
                requirements.getGroups().add(groupReq);
            }

            for (Room room : rooms) {
                RoomXml roomElement = new RoomXml();
                roomElement.setId("r" + room.getId());
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

//            for (Teacher teacher : teachers) {
//                for (int i = 0; i < 7; i++) {
//                    if (teacher.isFreeOnDay(i)) {
//                        TeacherFreeDay freeDay = new TeacherFreeDay();
//                        freeDay.setTeacherId(teacher.getId());
//                        freeDay.setDay(i);
//                        requirements.getFreeDays().add(freeDay);
//                    }
//                }
//            }

            for (Teacher teacher : teachers) {
                TeacherAvailableSlots availableSlots = new TeacherAvailableSlots();
                availableSlots.setTeacherId(teacher.getId());

                Map<Integer, List<XmlTimeSlot>> slotsByDay = new HashMap<>();

                for (TimeSlot timeSlot : teacher.getAvailableTimeSlots()) {
                    int slotDay = getDayOfWeek(timeSlot);

                    if (!slotsByDay.containsKey(slotDay)) {
                        slotsByDay.put(slotDay, new ArrayList<>());
                    }

                    XmlTimeSlot xmlSlot = new XmlTimeSlot();
                    xmlSlot.setStart((int) (timeSlot.getStartTime().getTime() / 1000));
                    xmlSlot.setEnd((int) (timeSlot.getEndTime().getTime() / 1000));
                    slotsByDay.get(slotDay).add(xmlSlot);
                }

                for (int day = 1; day <= 7; day++) {
                    if (slotsByDay.containsKey(day)) {
                        DaySlots daySlots = new DaySlots();
                        daySlots.setDayNumber(day);
                        daySlots.setTimeSlots(slotsByDay.get(day));
                        availableSlots.getAvailableSlots().add(daySlots);
                    }
                }

                requirements.getAvailableTeacherSlots().add(availableSlots);
            }
            JAXBContext context = JAXBContext.newInstance(Requirements.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(requirements, new File(filePath));

            System.out.println("XML file successfully generated: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getDayOfWeek(TimeSlot timeSlot) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeSlot.getStartTime());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
