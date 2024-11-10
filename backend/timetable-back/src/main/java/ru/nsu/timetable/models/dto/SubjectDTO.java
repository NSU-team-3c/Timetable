package ru.nsu.timetable.models.dto;

import java.util.Set;

public record SubjectDTO(Long id, String name, String type, int hours, String qualification,
                         Set<Long> occupiedTimeSlotsIds) {
}
