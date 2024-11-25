package ru.nsu.timetable.models.dto;

import java.util.Set;

public record CourseDTO(Long id, String name, Set<Long> groupIds, Set<Long> subjectIds) {
}
