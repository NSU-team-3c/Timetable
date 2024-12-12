package ru.nsu.timetable.models.mappers;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.StudentDTO;
import ru.nsu.timetable.models.dto.StudentRequestDTO;
import ru.nsu.timetable.models.entities.Student;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.repositories.UserRepository;
import ru.nsu.timetable.services.GroupService;

@RequiredArgsConstructor
@Component
public class StudentMapper {
    private final GroupService groupService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public StudentDTO toStudentDTO(Student student) {
        return new StudentDTO(student.getId(), student.getGroup().getId(), userMapper.toUserDTO(student.getUserData()));
    }

    public Student toStudent(StudentRequestDTO studentRequestDTO) {
        Student student = new Student();
        student.setGroup(groupService.getGroup(studentRequestDTO.groupId()));
        Optional<User> user = userRepository.findById(studentRequestDTO.userId());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + studentRequestDTO.userId() + " not found");
        } else {
            student.setUserData(user.get());
        }
        return student;
    }
}
