package ru.nsu.timetable.models.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.dto.TeacherRequestDTO;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.models.entities.Teacher;
import ru.nsu.timetable.repositories.SubjectRepository;

@Component
public class TeacherMapper {
    private final TimeSlotMapper timeSlotMapper;
    private final SubjectRepository subjectRepository;

    public TeacherMapper(TimeSlotMapper timeSlotMapper, SubjectRepository subjectRepository) {
        this.timeSlotMapper = timeSlotMapper;
        this.subjectRepository = subjectRepository;
    }

    public TeacherDTO toTeacherDTO(Teacher teacher) {
        return new TeacherDTO(
                teacher.getId(),
                teacher.getName(),
                teacher.getOrganisation(),
                teacher.getEducation(),
                teacher.getSpecialization(),
                teacher.getAvailableTimeSlots().stream()
                        .map(timeSlotMapper::toTimeSlotDTO)
                        .collect(Collectors.toList()),
                teacher.getSubjects().stream()
                        .map(Subject::getId)
                        .collect(Collectors.toList()),
                teacher.getUserId());
    }

    public Teacher toTeacher(TeacherRequestDTO teacherRequestDTO) {
        Teacher teacher = new Teacher();
        teacher.setName(teacherRequestDTO.name());
        teacher.setEducation(teacherRequestDTO.education());
        teacher.setSpecialization(teacherRequestDTO.specialization());
        teacher.setOrganisation(teacherRequestDTO.organization());
        return teacher;
    }
}
