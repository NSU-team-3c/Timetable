package ru.nsu.timetable.models.dto;

import java.util.Date;

public record TimeSlotDTO(
        Date startTime,
        Date endTime) {
}
