package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.TimeSlotDTO;
import ru.nsu.timetable.models.entities.TimeSlot;
import ru.nsu.timetable.repositories.RoomRepository;
import ru.nsu.timetable.repositories.SubjectRepository;
import ru.nsu.timetable.repositories.TeacherRepository;

@Component
public class TimeSlotMapper {
    private final SubjectRepository subjectRepository;
    private final RoomRepository roomRepository;
    private final TeacherRepository teacherRepository;

    public TimeSlotMapper(SubjectRepository subjectRepository, RoomRepository roomRepository,
                          TeacherRepository teacherRepository) {
        this.subjectRepository = subjectRepository;
        this.roomRepository = roomRepository;
        this.teacherRepository = teacherRepository;
    }

    public TimeSlotDTO toTimeSlotDTO(TimeSlot timeSlot) {
        return new TimeSlotDTO(timeSlot.getId(), timeSlot.getStartTime(), timeSlot.getEndTime(),
                timeSlot.getSubject().getId(), timeSlot.getRoom().getId(), timeSlot.getTeacher().getId());
    }

    public TimeSlot toTimeSlot(TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(timeSlotDTO.startTime());
        timeSlot.setEndTime(timeSlotDTO.endTime());
        subjectRepository.findById(timeSlotDTO.subjectId()).ifPresent(timeSlot::setSubject);
        roomRepository.findById(timeSlotDTO.roomId()).ifPresent(timeSlot::setRoom);
        teacherRepository.findById(timeSlotDTO.teacherId()).ifPresent(timeSlot::setTeacher);
        if (timeSlotDTO.id() != null) {
            timeSlot.setId(timeSlotDTO.id());
        }
        return timeSlot;
    }
}
