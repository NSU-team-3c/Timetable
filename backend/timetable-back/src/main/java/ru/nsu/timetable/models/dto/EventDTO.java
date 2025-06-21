package ru.nsu.timetable.models.dto;

import java.util.Date;

public record EventDTO(
        Long id,
        Date startTime,
        Date endTime,
        String teacherName,
        String subjectName,
        String roomName,
        String audienceType
) {}
