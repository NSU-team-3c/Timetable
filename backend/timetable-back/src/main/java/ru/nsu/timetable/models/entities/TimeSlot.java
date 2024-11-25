package ru.nsu.timetable.models.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date startTime;
    private Date endTime;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Room room;

    @ManyToOne
    private Teacher teacher;
}
