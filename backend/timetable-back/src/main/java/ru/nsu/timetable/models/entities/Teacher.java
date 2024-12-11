package ru.nsu.timetable.models.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String organisation;

    private String education;

    private String specialization;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "teacher_id")
    private List<TimeSlot> availableTimeSlots = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects = new ArrayList<>();

    private long userId;
}
