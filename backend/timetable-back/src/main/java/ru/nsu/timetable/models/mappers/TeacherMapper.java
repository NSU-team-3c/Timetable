package ru.nsu.timetable.models.mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.entities.Teacher;

@Component
public class TeacherMapper {
    private final TimeSlotMapper timeSlotMapper;

    public TeacherMapper(TimeSlotMapper timeSlotMapper) {
        this.timeSlotMapper = timeSlotMapper;
    }

    public TeacherDTO toTeacherDTO(Teacher teacher) {
        return new TeacherDTO(
                teacher.getId(),
                teacher.getName(),
                teacher.getAvailableTimeSlots().stream()
                        .map(timeSlotMapper::toTimeSlotDTO)
                        .collect(Collectors.toList()),
                teacher.getQualification(),
                teacher.getUserId(),
                teacher.getFreeDays() != null
                        ? new HashSet<>(teacher.getFreeDays())
                        : Set.of()
        );
    }

    public Teacher toTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = new Teacher();
        teacher.setName(teacherDTO.name());
        teacher.setAvailableTimeSlots(teacherDTO.availableTimeSlots().stream()
                .map(timeSlotMapper::toTimeSlot)
                .collect(Collectors.toList()));
        teacher.setQualification(teacherDTO.qualification());
        teacher.setUserId(teacherDTO.userId());
        if (teacherDTO.id() != null) {
            teacher.setId(teacherDTO.id());
        }
        return teacher;
    }
}
