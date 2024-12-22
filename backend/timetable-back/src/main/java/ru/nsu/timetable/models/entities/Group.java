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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number", unique = true)
    private String number;

    private String course;

    private String department;

    private int capacity;

    @ManyToMany(mappedBy = "groups")
    @Builder.Default
    private List<Subject> subjects = new ArrayList<>();

    @ManyToMany(mappedBy = "groups")
    @Builder.Default
    private List<Event> events = new ArrayList<>();
}
