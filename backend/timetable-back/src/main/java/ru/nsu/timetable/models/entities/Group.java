package ru.nsu.timetable.models.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number", unique = true)
    private String number;

    private List<String> students = new ArrayList<>();

    @ManyToMany(mappedBy = "groups")
    private List<Subject> subjects = new ArrayList<>();

    @ManyToMany(mappedBy = "groups")
    private List<Event> events;
}
