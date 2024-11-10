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
import ru.nsu.timetable.models.dto.ConstraintDTO;
import ru.nsu.timetable.models.entities.Constraint;
import ru.nsu.timetable.models.mappers.ConstraintMapper;
import ru.nsu.timetable.services.ConstraintService;

@RestController
@RequestMapping("/constraints")
public class ConstraintController {
    private final ConstraintService constraintService;
    private final ConstraintMapper constraintMapper;

    @Autowired
    public ConstraintController(ConstraintService constraintService, ConstraintMapper constraintMapper) {
        this.constraintService = constraintService;
        this.constraintMapper = constraintMapper;
    }

    @GetMapping
    public List<ConstraintDTO> getAllConstraints() {
        return constraintService.getAllConstraints()
                .stream()
                .map(constraintMapper::toConstraintDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConstraintDTO> getConstraintById(@PathVariable Long id) {
        return constraintService.getConstraintById(id)
                .map(constraintMapper::toConstraintDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ConstraintDTO> createConstraint(@RequestBody ConstraintDTO constraintDTO) {
        Constraint constraint = constraintMapper.toConstraint(constraintDTO);
        Constraint savedConstraint = constraintService.saveConstraint(constraint);
        return ResponseEntity.created(URI.create("/constraints/" + savedConstraint.getId()))
                .body(constraintMapper.toConstraintDTO(savedConstraint));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConstraint(@PathVariable Long id) {
        Optional<Constraint> constraint = constraintService.getConstraintById(id);
        if (constraint.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        constraintService.deleteConstraint(id);
        return ResponseEntity.noContent().build();
    }
}
