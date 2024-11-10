package ru.nsu.timetable.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.entities.Teacher;
import ru.nsu.timetable.models.mappers.TeacherMapper;
import ru.nsu.timetable.services.TeacherService;

@RestController
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherController(TeacherService teacherService, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping
    public List<TeacherDTO> getAllTeachers() {
        return teacherService.getAllTeachers()
                .stream()
                .map(teacherMapper::toTeacherDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id)
                .map(teacherMapper::toTeacherDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) {
        Teacher teacher = teacherMapper.toTeacher(teacherDTO);
        Teacher savedTeacher = teacherService.saveTeacher(teacher);
        return ResponseEntity.created(URI.create("/teachers/" + savedTeacher.getId()))
                .body(teacherMapper.toTeacherDTO(savedTeacher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        Optional<Teacher> teacher = teacherService.getTeacherById(id);
        if (teacher.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
