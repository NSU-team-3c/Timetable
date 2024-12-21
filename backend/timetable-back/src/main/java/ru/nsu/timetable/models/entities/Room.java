package ru.nsu.timetable.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number", unique = true)
    private String number;

    private int capacity;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    public enum RoomType {
        computer, online, lecture, common
    }

}
