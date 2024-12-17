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

                groupReq.setAmount(group.getCapacity());

                List<Requirement> reqList = new ArrayList<>();
                for (Subject subject : group.getSubjects()) {
                    for (Teacher teacher : subject.getTeachers()) {
                        Requirement req = new Requirement();
                        req.setSubjectId(subject.getId());
                        req.setTeacherId(teacher.getId());
                        req.setAmount(subject.getDuration());
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

            for (Teacher teacher : teachers) {
                TeacherAvailableSlots availableSlots = new TeacherAvailableSlots();
                availableSlots.setTeacherId(teacher.getId());

                Map<Integer, List<XmlTimeSlot>> slotsByDay = new HashMap<>();

                for (TimeSlot timeSlot : teacher.getAvailableTimeSlots()) {
                    int slotDay = getDayOfWeek(timeSlot);

                    if (!slotsByDay.containsKey(slotDay)) {
                        slotsByDay.put(slotDay, new ArrayList<>());
                    }

                    int pairNumber = getPairNumber(timeSlot);

                    if (pairNumber != -1) {
                        XmlTimeSlot xmlSlot = new XmlTimeSlot();
                        xmlSlot.setDay(slotDay);
                        xmlSlot.setStart(pairNumber);
                        xmlSlot.setEnd(pairNumber);

                        slotsByDay.get(slotDay).add(xmlSlot);
                    }
                }

                for (Map.Entry<Integer, List<XmlTimeSlot>> entry : slotsByDay.entrySet()) {
                    int day = entry.getKey();
                    List<XmlTimeSlot> slots = entry.getValue();

                    if (!slots.isEmpty()) {
                        for (XmlTimeSlot slot : slots) {
                            availableSlots.getSlots().add(slot);
                        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getPairNumber(TimeSlot timeSlot) {
        Map<Integer, PairRange> pairRanges = new HashMap<>();
        pairRanges.put(1, new PairRange("09:00", "10:35"));
        pairRanges.put(2, new PairRange("10:50", "12:25"));
        pairRanges.put(3, new PairRange("12:40", "14:15"));
        pairRanges.put(4, new PairRange("14:30", "16:05"));
        pairRanges.put(5, new PairRange("16:20", "17:55"));
        pairRanges.put(6, new PairRange("18:10", "19:45"));
        pairRanges.put(7, new PairRange("20:00", "21:35"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeSlot.getStartTime());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SUNDAY) {
            return -1;
        }

        for (Map.Entry<Integer, PairRange> entry : pairRanges.entrySet()) {
            PairRange range = entry.getValue();
            if (isTimeInRange(hour, minute, range.getStartTime(), range.getEndTime())) {
                return entry.getKey();
            }
        }

        return -1;
    }

    private boolean isTimeInRange(int hour, int minute, String startTime, String endTime) {
        String[] start = startTime.split(":");
        String[] end = endTime.split(":");

        int startHour = Integer.parseInt(start[0]);
        int startMinute = Integer.parseInt(start[1]);
        int endHour = Integer.parseInt(end[0]);
        int endMinute = Integer.parseInt(end[1]);

        int currentTimeInMinutes = hour * 60 + minute;
        int startTimeInMinutes = startHour * 60 + startMinute;
        int endTimeInMinutes = endHour * 60 + endMinute;

        return currentTimeInMinutes >= startTimeInMinutes && currentTimeInMinutes < endTimeInMinutes;
    }

    @Data
    public static class PairRange {
        private String startTime;
        private String endTime;

        public PairRange(String startTime, String endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    private int getDayOfWeek(TimeSlot timeSlot) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeSlot.getStartTime());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
