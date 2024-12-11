package ru.nsu.timetable.controllers;

import java.util.List;

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
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.dto.TeacherRequestDTO;
import ru.nsu.timetable.models.dto.TeacherSubjectDTO;
import ru.nsu.timetable.models.mappers.TeacherMapper;
import ru.nsu.timetable.services.TeacherService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/management")
@Tag(name = "Teacher controller")
public class TeacherController {
    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    @GetMapping("/teachers")
    public List<TeacherDTO> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/teachers/{id}")
    public TeacherDTO getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }

    @PostMapping("/teachers")
    public TeacherDTO createTeacher(@RequestBody TeacherRequestDTO teacherRequestDTO) {
        return teacherService.saveTeacher(teacherRequestDTO);
    }

    @DeleteMapping("/teachers/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
    }

    @PutMapping("teachers/{id}")
    public TeacherDTO updateTeacher(@PathVariable long id, @RequestBody TeacherRequestDTO teacherRequestDTO) {
        return teacherService.updateTeacher(id, teacherRequestDTO);
    }

    @PostMapping("teachers/assign-subjects")
    public TeacherDTO assignSubjectsToTeacher(@RequestBody TeacherSubjectDTO dto) {
        return teacherService.assignSubjectsToTeacher(dto);
    }

    @PutMapping("teachers/update-subjects")
    public TeacherDTO updateSubjectsForTeacher(@RequestBody TeacherSubjectDTO dto) {
        return teacherService.updateSubjectsForTeacher(dto);
    }

    @DeleteMapping("teachers/remove-subjects")
    public TeacherDTO removeSubjectsFromTeacher(@RequestBody TeacherSubjectDTO dto) {
        return teacherService.removeSubjectsFromTeacher(dto);
    }
}
