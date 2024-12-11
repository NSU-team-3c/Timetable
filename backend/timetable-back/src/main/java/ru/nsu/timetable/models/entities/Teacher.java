package ru.nsu.timetable.models.entities;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "teacher_id")
    private List<TimeSlot> availableTimeSlots = new ArrayList<>();

    private String qualification;

    private long userId;

    @ElementCollection
    private Set<Integer> freeDays;
}
