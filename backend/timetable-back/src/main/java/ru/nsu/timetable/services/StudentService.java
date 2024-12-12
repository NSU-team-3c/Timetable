package ru.nsu.timetable.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.StudentDTO;
import ru.nsu.timetable.models.dto.StudentRequestDTO;
import ru.nsu.timetable.models.entities.Group;
import ru.nsu.timetable.models.entities.Student;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.models.mappers.StudentMapper;
import ru.nsu.timetable.repositories.GroupRepository;
import ru.nsu.timetable.repositories.StudentRepository;
import ru.nsu.timetable.repositories.UserRepository;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public List<StudentDTO> getAllStudents() {
        return studentRepository
                .findAll()
                .stream()
                .map(studentMapper::toStudentDTO)
                .toList();
    }

    public StudentDTO getStudentByUserId(Long userId) {
        Optional<Student> student = studentRepository.findByUserData_id(userId);
        if (student.isEmpty()) {
            throw new ResourceNotFoundException("Teacher with user id " + userId + " not found");
        }
        return studentMapper.toStudentDTO(student.get());
    }

    public StudentDTO getStudentById(Long id) {
        return studentMapper.toStudentDTO(getStudent(id));
    }

    public StudentDTO saveStudent(StudentRequestDTO dto) {
        Student student = studentMapper.toStudent(dto);
        return studentMapper.toStudentDTO(studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
    }

    public StudentDTO updateStudent(Long id, StudentRequestDTO studentRequestDTO) {
        Student student = getStudent(id);
        if (studentRequestDTO.groupId() > 0) {
            Optional<Group> group = groupRepository.findById(studentRequestDTO.groupId());
            if (group.isEmpty()) {
                throw new ResourceNotFoundException("Group with id " + id + " not found");
            } else {
                student.setGroup(group.get());
            }
        }
        if (studentRequestDTO.userId() > 0) {
            Optional<User> user = userRepository.findById(studentRequestDTO.userId());
            if (user.isEmpty()) {
                throw new ResourceNotFoundException("User with id " + id + " not found");
            } else {
                student.setUserData(user.get());
            }
        }
        return studentMapper.toStudentDTO(studentRepository.save(student));
    }

    private Student getStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        } else {
            return student.get();
        }
    }
}
