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
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private int capacity;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    @OneToMany(mappedBy = "room")
    private Set<TimeSlot> occupiedTimeSlots = new HashSet<>();

    public void addOccupiedTimeSlot(TimeSlot timeSlot) {
        occupiedTimeSlots.add(timeSlot);
        timeSlot.setRoom(this);
    }

    public enum RoomType {
        LECTURE_ROOM, COMPUTER_ROOM, LABORATORY, SEMINAR_ROOM
    }

}
