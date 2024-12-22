package ru.nsu.timetable.models.dto;

import java.util.List;

public record SubjectRequestDTO(
        String name,
        String code,
        String description,
        Integer duration,
        String audienceType,
        List<Long> teacherIds,
        List<Long> groupIds) {
}
