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
    public SubjectDTO createSubject(@RequestBody SubjectRequestDTO subjectRequestDTO) {
        return subjectService.saveSubject(subjectRequestDTO);
    }

    @DeleteMapping("/subjects/{id}")
    public void deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
    }

    @PutMapping("subjects/{id}")
    public SubjectDTO updateSubject(@PathVariable long id, @RequestBody SubjectRequestDTO subjectRequestDTO) {
        return subjectService.updateSubject(id, subjectRequestDTO);
    }

    @PostMapping("subjects/assign-groups")
    public SubjectDTO assignGroupsToSubject(@RequestBody SubjectGroupDTO dto) {
        return subjectService.assignGroupsToSubject(dto);
    }

    @PutMapping("subjects/update-groups")
    public SubjectDTO updateGroupsForSubject(@RequestBody SubjectGroupDTO dto) {
        return subjectService.updateGroupsForSubject(dto);
    }

    @DeleteMapping("subjects/remove-groups")
    public SubjectDTO removeGroupsFromSubject(@RequestBody SubjectGroupDTO dto) {
        return subjectService.removeGroupsFromSubject(dto);
    }
}