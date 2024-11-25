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
import ru.nsu.timetable.models.dto.ConstraintDTO;
import ru.nsu.timetable.services.ConstraintService;

@RestController
@RequestMapping("/api/v1/management")
@RequiredArgsConstructor
public class ConstraintController {
    private final ConstraintService constraintService;

    @GetMapping("/constraints")
    public List<ConstraintDTO> getAllConstraints() {
        return constraintService.getAllConstraints();
    }

    @GetMapping("/constraints/{id}")
    public ConstraintDTO getConstraintById(@PathVariable Long id) {
        return constraintService.getConstraintById(id);
    }

    @PostMapping("/constraints")
    public ConstraintDTO createConstraint(@RequestBody ConstraintDTO constraintDTO) {
        return constraintService.saveConstraint(constraintDTO);
    }

    @DeleteMapping("/constraints/{id}")
    public void deleteConstraint(@PathVariable Long id) {
        constraintService.deleteConstraint(id);
    }
}
