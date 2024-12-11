package ru.nsu.timetable.models.dto;

import java.util.List;

public record TimetableDTO(Long id, List<EventDTO> events) {
}
