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
import ru.nsu.timetable.models.dto.FacultyDTO;
import ru.nsu.timetable.models.entities.Faculty;
import ru.nsu.timetable.models.mappers.FacultyMapper;
import ru.nsu.timetable.services.FacultyService;

@RestController
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;
    private final FacultyMapper facultyMapper;

    @Autowired
    public FacultyController(FacultyService facultyService, FacultyMapper facultyMapper) {
        this.facultyService = facultyService;
        this.facultyMapper = facultyMapper;
    }

    @GetMapping
    public List<FacultyDTO> getAllFaculties() {
        return facultyService.getAllFaculties()
                .stream()
                .map(facultyMapper::toFacultyDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyDTO> getFacultyById(@PathVariable Long id) {
        return facultyService.getFacultyById(id)
                .map(facultyMapper::toFacultyDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FacultyDTO> createFaculty(@RequestBody FacultyDTO facultyDTO) {
        Faculty faculty = facultyMapper.toFaculty(facultyDTO);
        Faculty savedCourse = facultyService.saveFaculty(faculty);
        return ResponseEntity.created(URI.create("/faculties/" + savedCourse.getId()))
                .body(facultyMapper.toFacultyDTO(savedCourse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        Optional<Faculty> faculty = facultyService.getFacultyById(id);
        if (faculty.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}