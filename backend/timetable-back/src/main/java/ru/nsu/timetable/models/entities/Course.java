package ru.nsu.timetable.models.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany
    private Set<Group> groups = new HashSet<>();

    @OneToMany
    private Set<Subject> subjects = new HashSet<>();

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }
}
