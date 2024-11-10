package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.ConstraintDTO;
import ru.nsu.timetable.models.entities.Constraint;

@Component
public class ConstraintMapper {
    public ConstraintDTO toConstraintDTO(Constraint constraint) {
        return new ConstraintDTO(constraint.getId(), constraint.getType().name(),
                constraint.getPriority().name());
    }

    public Constraint toConstraint(ConstraintDTO constraintDTO) {
        Constraint constraint = new Constraint();
        constraint.setType(Constraint.ConstraintType.valueOf(constraintDTO.type()));
        constraint.setPriority(Constraint.ConstraintPriority.valueOf(constraintDTO.priority()));
        if (constraintDTO.id() != null) {
            constraint.setId(constraintDTO.id());
        }
        return constraint;
    }
}
