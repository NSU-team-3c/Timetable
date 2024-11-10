package ru.nsu.timetable.models.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private SubjectType type;

    private int hours;

    private String qualification;

    @OneToMany(mappedBy = "subject")
    private Set<TimeSlot> occupiedTimeSlots = new HashSet<>();

    public void addOccupiedTimeSlot(TimeSlot timeSlot) {
        occupiedTimeSlots.add(timeSlot);
        timeSlot.setSubject(this);
    }

    public enum SubjectType {
        LECTURE, COMPUTER_SEMINAR, LABORATORY_SEMINAR, SEMINAR
    }
}
