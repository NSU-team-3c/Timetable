package ru.nsu.timetable.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "constraints")
public class Constraint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private ConstraintType type;

    @Enumerated(EnumType.STRING)
    private ConstraintPriority priority;

    public enum ConstraintType {
        ROOM_INFO, ROOM_AVAILABILITY, GROUP_INFO, GROUP_STRUCTURE, COURSE_INFO, COURSE_STRUCTURE,
        FACULTY_INFO, FACULTY_STRUCTURE, SUBJECT_INFO, GROUPS_PER_SUBJECTS, STUDENT_INFO, TEACHER_INFO,
        TEACHER_AVAILABILITY, TEACHER_ABILITY_TO_TEACH, GENERAL_POLICIES
    }

    public enum ConstraintPriority {
        LOW, MEDIUM, HIGH
    }

}
