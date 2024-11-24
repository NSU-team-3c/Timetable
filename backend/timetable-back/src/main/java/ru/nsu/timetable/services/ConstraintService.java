package ru.nsu.timetable.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.ConstraintDTO;
import ru.nsu.timetable.models.entities.Constraint;
import ru.nsu.timetable.models.mappers.ConstraintMapper;
import ru.nsu.timetable.repositories.ConstraintRepository;

@Service
@RequiredArgsConstructor
public class ConstraintService {
    private final ConstraintRepository constraintRepository;
    private final ConstraintMapper constraintMapper;

    public List<ConstraintDTO> getAllConstraints() {
        return constraintRepository
                .findAll()
                .stream()
                .map(constraintMapper::toConstraintDTO)
                .toList();
    }

    public ConstraintDTO getConstraintById(Long id) {
        return constraintMapper.toConstraintDTO(getConstraint(id));
    }

    public ConstraintDTO saveConstraint(ConstraintDTO constraintDTO) {
        Constraint constraint = constraintMapper.toConstraint(constraintDTO);
        return constraintMapper.toConstraintDTO(constraintRepository.save(constraint));
    }

    public void deleteConstraint(Long id) {
        if (constraintRepository.existsById(id)) {
            constraintRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Constraint with id " + id + " not found");
        }
    }

    private Constraint getConstraint(Long id) {
        Optional<Constraint> constraint= constraintRepository.findById(id);
        if (constraint.isEmpty()) {
            throw new ResourceNotFoundException("Constraint with id " + id + " not found");
        }
        else {
            return constraint.get();
        }
    }
}
