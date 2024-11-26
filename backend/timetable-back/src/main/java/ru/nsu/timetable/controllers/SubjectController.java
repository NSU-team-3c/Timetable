package ru.nsu.timetable.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.models.dto.SubjectDTO;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.models.mappers.SubjectMapper;
import ru.nsu.timetable.services.SubjectService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/management")
@Tag(name = "Subject controller")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/subjects")
    public List<SubjectDTO> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/subjects/{id}")
    public SubjectDTO getSubjectById(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }

    @PostMapping("/subjects")
    public SubjectDTO createSubject(@RequestBody SubjectDTO subjectDTO) {
        return subjectService.saveSubject(subjectDTO);
    }

    @DeleteMapping("/subjects/{id}")
    public void deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
    }
}