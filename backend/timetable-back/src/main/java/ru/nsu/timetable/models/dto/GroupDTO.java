package ru.nsu.timetable.models.dto;

import java.util.Set;

public record GroupDTO(Long id, String name, Set<Long> studentIds) {
}
