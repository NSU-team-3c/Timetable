package ru.nsu.timetable.models.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany(mappedBy = "teacher")
    private Set<TimeSlot> occupiedTimeSlots = new HashSet<>();

    private String qualification;

    private long userId;

    @ElementCollection
    private Set<Integer> freeDays;

    public void addOccupiedTimeSlot(TimeSlot timeSlot) {
        occupiedTimeSlots.add(timeSlot);
        timeSlot.setTeacher(this);
    }
}
