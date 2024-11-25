package ru.nsu.timetable.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.timetable.models.entities.Constraint;

@Repository
public interface ConstraintRepository extends JpaRepository<Constraint, Long> {
}