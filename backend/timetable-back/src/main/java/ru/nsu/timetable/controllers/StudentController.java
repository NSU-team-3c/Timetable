package ru.nsu.timetable.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.StudentDTO;
import ru.nsu.timetable.models.dto.StudentRequestDTO;
import ru.nsu.timetable.services.StudentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/students")
@Tag(name = "Student controller")
public class StudentController {
    private final StudentService studentService;

    @GetMapping("")
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{userId}")
    @Operation(description = "Получение студента по id пользователя.")
    public StudentDTO getStudentByUserId(@PathVariable Long userId) {
        return studentService.getStudentByUserId(userId);
    }

    @PostMapping("")
    public StudentDTO createStudent(@RequestBody StudentRequestDTO dto) {
        return studentService.saveStudent(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @PutMapping("/{id}")
    public StudentDTO updateStudent(@PathVariable Long id, StudentRequestDTO dto) {
        return studentService.updateStudent(id, dto);
    }
}
