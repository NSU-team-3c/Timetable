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
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.dto.TeacherRequestDTO;
import ru.nsu.timetable.models.dto.TeacherSubjectDTO;
import ru.nsu.timetable.models.dto.TeacherTimeslotDTO;
import ru.nsu.timetable.services.TeacherService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/teachers")
@Tag(name = "Teacher controller")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("")
    public List<TeacherDTO> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{userId}")
    @Operation(description = "Получение учителя по id пользователя.")
    public TeacherDTO getTeacherByUserId(@PathVariable Long userId) {
        return teacherService.getTeacherByUserId(userId);
    }

    @PostMapping("")
    public TeacherDTO createTeacher(@RequestBody TeacherRequestDTO teacherRequestDTO) {
        return teacherService.saveTeacher(teacherRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
    }

    @PutMapping("/{id}")
    public TeacherDTO updateTeacher(@PathVariable long id, @RequestBody TeacherRequestDTO teacherRequestDTO) {
        return teacherService.updateTeacher(id, teacherRequestDTO);
    }

    @PostMapping("/assign-subjects")
    public TeacherDTO assignSubjectsToTeacher(@RequestBody TeacherSubjectDTO dto) {
        return teacherService.assignSubjectsToTeacher(dto);
    }

    @PutMapping("/update-subjects")
    public TeacherDTO updateSubjectsForTeacher(@RequestBody TeacherSubjectDTO dto) {
        return teacherService.updateSubjectsForTeacher(dto);
    }

    @DeleteMapping("/remove-subjects")
    public TeacherDTO removeSubjectsFromTeacher(@RequestBody TeacherSubjectDTO dto) {
        return teacherService.removeSubjectsFromTeacher(dto);
    }

    @PostMapping("/teacher-availability")
    public TeacherDTO assignTimeSlotsToTeacher(@RequestBody TeacherTimeslotDTO dto) {
        return teacherService.assignTimeSlotsToTeacher(dto);
    }

    @PutMapping("/update-availability")
    public TeacherDTO updateTimeSlotsForTeacher(@RequestBody TeacherTimeslotDTO dto) {
        return teacherService.updateTimeSlotsForTeacher(dto);
    }

    @DeleteMapping("/remove-availability")
    public TeacherDTO removeTimeSlotsFromTeacher(@RequestBody TeacherTimeslotDTO dto) {
        return teacherService.removeTimeSlotsFromTeacher(dto);
    }
}
