package ru.nsu.timetable.models.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.entities.Teacher;
import ru.nsu.timetable.models.entities.TimeSlot;
import ru.nsu.timetable.repositories.TimeSlotRepository;

@Component
public class TeacherMapper {
    private final TimeSlotRepository timeSlotRepository;

    public TeacherMapper(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public TeacherDTO toTeacherDTO(Teacher teacher) {
        return new TeacherDTO(teacher.getId(), teacher.getName(),
                teacher.getOccupiedTimeSlots().stream()
                        .map(TimeSlot::getId)
                        .collect(Collectors.toSet()),
                teacher.getQualification(),
                teacher.getUserId());
    }

    public Teacher toTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = new Teacher();
        teacher.setName(teacherDTO.name());
        teacherDTO.occupiedTimeSlotsIds().forEach(timeSlotId ->
                timeSlotRepository.findById(timeSlotId).ifPresent(teacher::addOccupiedTimeSlot)
        );
        teacher.setQualification(teacherDTO.qualification());
        teacher.setUserId(teacherDTO.userId());
        if (teacherDTO.id() != null) {
            teacher.setId(teacherDTO.id());
        }
        return teacher;
    }
}
