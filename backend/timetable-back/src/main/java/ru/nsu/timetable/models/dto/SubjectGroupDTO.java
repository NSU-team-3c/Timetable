package ru.nsu.timetable.models.dto;

import java.util.List;

public record SubjectGroupDTO(Long subjectId, List<Long> groupIds) {
}
