package ru.nsu.timetable.models.dto;

import java.util.Set;

public record FacultyDTO(Long id, String name, Set<Long> courseIds) {
}
