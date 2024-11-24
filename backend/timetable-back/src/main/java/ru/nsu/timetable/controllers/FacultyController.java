package ru.nsu.timetable.controllers;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.FacultyDTO;
import ru.nsu.timetable.services.FacultyService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/management")
public class FacultyController {
    private final FacultyService facultyService;

    @GetMapping("faculties")
    public List<FacultyDTO> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @GetMapping("/faculties/{id}")
    public FacultyDTO getFacultyById(@PathVariable Long id) {
        return facultyService.getFacultyById(id);
    }

    @PostMapping("faculties")
    public FacultyDTO createFaculty(@RequestBody FacultyDTO facultyDTO) {
        return facultyService.saveFaculty(facultyDTO);
    }

    @DeleteMapping("/faculties/{id}")
    public void deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
    }
}