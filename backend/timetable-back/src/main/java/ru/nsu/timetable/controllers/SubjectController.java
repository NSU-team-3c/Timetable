package ru.nsu.timetable.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.models.dto.SubjectDTO;
import ru.nsu.timetable.models.dto.SubjectGroupDTO;
import ru.nsu.timetable.models.dto.SubjectRequestDTO;
import ru.nsu.timetable.services.SubjectService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/subjects")
@Tag(name = "Subject controller")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("")
    public List<SubjectDTO> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}")
    public SubjectDTO getSubjectById(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }

    @PostMapping("")
    public SubjectDTO createSubject(@RequestBody SubjectRequestDTO subjectRequestDTO) {
        return subjectService.saveSubject(subjectRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
    }

    @PutMapping("/{id}")
    public SubjectDTO updateSubject(@PathVariable long id, @RequestBody SubjectRequestDTO subjectRequestDTO) {
        return subjectService.updateSubject(id, subjectRequestDTO);
    }

    @PostMapping("/assign-groups")
    public SubjectDTO assignGroupsToSubject(@RequestBody SubjectGroupDTO dto) {
        return subjectService.assignGroupsToSubject(dto);
    }

    @PutMapping("/update-groups")
    public SubjectDTO updateGroupsForSubject(@RequestBody SubjectGroupDTO dto) {
        return subjectService.updateGroupsForSubject(dto);
    }

    @DeleteMapping("/remove-groups")
    public SubjectDTO removeGroupsFromSubject(@RequestBody SubjectGroupDTO dto) {
        return subjectService.removeGroupsFromSubject(dto);
    }
}