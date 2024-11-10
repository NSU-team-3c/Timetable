package ru.nsu.timetable.models.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    public void addOccupiedTimeSlot(TimeSlot timeSlot) {
        occupiedTimeSlots.add(timeSlot);
        timeSlot.setTeacher(this);
    }
}
