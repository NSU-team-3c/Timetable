package ru.nsu.timetable.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.models.dto.SubjectDTO;
import ru.nsu.timetable.models.dto.SubjectRequestDTO;
import ru.nsu.timetable.services.SubjectService;
import ru.nsu.timetable.sockets.MessageUtils;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/subjects")
@Tag(name = "Subject controller")
public class SubjectController {
    private final SubjectService subjectService;
    private final MessageUtils messageUtils;

    @GetMapping("")
    public List<SubjectDTO> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}")
    public SubjectDTO getSubjectById(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }

    @PostMapping("")
    @Operation(summary = "Create subject", security = @SecurityRequirement(name = "bearerAuth"))
    public SubjectDTO createSubject(HttpServletRequest request, @RequestBody SubjectRequestDTO subjectRequestDTO) {
        SubjectDTO subjectDTO = subjectService.saveSubject(subjectRequestDTO);
        messageUtils.sendMessage(request, "subject", "subject " + subjectDTO.name() + " created", null);
        return subjectDTO;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subject", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteSubject(HttpServletRequest request, @PathVariable Long id) {
        String deletedSubjectName = subjectService.deleteSubject(id);
        messageUtils.sendMessage(request, "subject", "subject " + deletedSubjectName + " deleted", null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update subject", security = @SecurityRequirement(name = "bearerAuth"))
    public SubjectDTO updateSubject(HttpServletRequest request, @PathVariable long id, @RequestBody SubjectRequestDTO subjectRequestDTO) {
        SubjectDTO subjectDTO = subjectService.updateSubject(id, subjectRequestDTO);
        messageUtils.sendMessage(request, "subject", "subject " + subjectDTO.name() + " created", null);
        return subjectDTO;
    }
}