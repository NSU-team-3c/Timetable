package ru.nsu.timetable.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.models.dto.SubjectDTO;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.models.mappers.SubjectMapper;
import ru.nsu.timetable.services.SubjectService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {
    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;

    @Autowired
    public SubjectController(SubjectService subjectService, SubjectMapper subjectMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;

    }

    @GetMapping
    public List<SubjectDTO> getAllSubjects() {
        return subjectService.getAllSubjects()
                .stream()
                .map(subjectMapper::toSubjectDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        return subjectService.getSubjectById(id)
                .map(subjectMapper::toSubjectDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SubjectDTO> createSubject(@RequestBody SubjectDTO subjectDTO) {
        Subject subject = subjectMapper.toSubject(subjectDTO);
        Subject savedSubject = subjectService.saveSubject(subject);
        return ResponseEntity.created(URI.create("/subjects/" + savedSubject.getId()))
                .body(subjectMapper.toSubjectDTO(savedSubject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        if (subjectService.getSubjectById(id).isPresent()) {
            subjectService.deleteSubject(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}