package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.StudentDTO;
import ru.nsu.timetable.models.entities.Student;

@Component
public class StudentMapper {
    public StudentDTO toStudentDTO(Student student) {
        return new StudentDTO(student.getId(), student.getName(), student.getUserId());
    }

    public Student toStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setName(studentDTO.name());
        student.setUserId(studentDTO.userId());
        if (studentDTO.id() != null) {
            student.setId(studentDTO.id());
        }
        return student;
    }
}
