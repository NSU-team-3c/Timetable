package ru.nsu.timetable.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.StudentDTO;
import ru.nsu.timetable.models.entities.Student;
import ru.nsu.timetable.models.mappers.StudentMapper;
import ru.nsu.timetable.repositories.StudentRepository;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public List<StudentDTO> getAllStudents() {
        return studentRepository
                .findAll()
                .stream()
                .map(studentMapper::toStudentDTO)
                .toList();
    }

    public StudentDTO getStudentById(Long id) {
        return studentMapper.toStudentDTO(getStudent(id));
    }

    public StudentDTO saveStudent(StudentDTO studentDTO) {
        Student student = studentMapper.toStudent(studentDTO);
        return studentMapper.toStudentDTO(studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
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
