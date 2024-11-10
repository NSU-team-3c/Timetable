package ru.nsu.timetable.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.models.entities.Constraint;
import ru.nsu.timetable.repositories.ConstraintRepository;

@Service
public class ConstraintService {
    private final ConstraintRepository constraintRepository;

    @Autowired
    public ConstraintService(ConstraintRepository constraintRepository) {
        this.constraintRepository = constraintRepository;
    }

    public List<Constraint> getAllConstraints() {
        return constraintRepository.findAll();
    }

    public Optional<Constraint> getConstraintById(Long id) {
        return constraintRepository.findById(id);
    }

    public Constraint saveConstraint(Constraint constraint) {
        return constraintRepository.save(constraint);
    }

    public void deleteConstraint(Long id) {
        constraintRepository.deleteById(id);
    }
}
