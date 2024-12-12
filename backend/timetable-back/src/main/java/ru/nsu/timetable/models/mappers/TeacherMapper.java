package ru.nsu.timetable.models.mappers;

import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.dto.TeacherRequestDTO;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.models.entities.Teacher;
import ru.nsu.timetable.models.entities.TimeSlot;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.repositories.UserRepository;

@RequiredArgsConstructor
@Component
public class TeacherMapper {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public TeacherDTO toTeacherDTO(Teacher teacher) {
        return new TeacherDTO(
                teacher.getId(),
                teacher.getOrganisation(),
                teacher.getEducation(),
                teacher.getSpecialization(),
                teacher.getAvailableTimeSlots().stream()
                        .map(TimeSlot::getId)
                        .collect(Collectors.toList()),
                teacher.getSubjects().stream()
                        .map(Subject::getId)
                        .collect(Collectors.toList()),
                userMapper.toUserDTO(teacher.getUserData()));
    }

    public Teacher toTeacher(TeacherRequestDTO teacherRequestDTO) {
        Teacher teacher = new Teacher();
        teacher.setEducation(teacherRequestDTO.education());
        teacher.setSpecialization(teacherRequestDTO.specialization());
        teacher.setOrganisation(teacherRequestDTO.organization());
        Optional<User> user = userRepository.findById(teacherRequestDTO.userId());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + teacherRequestDTO.userId() + " not found");
        } else {
            teacher.setUserData(user.get());
        }
        return teacher;
    }
}
