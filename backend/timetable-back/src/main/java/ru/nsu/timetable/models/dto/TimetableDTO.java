package ru.nsu.timetable.models.dto;

import java.util.Set;

public record TimetableDTO(Long id, Set<Long> facultyIds, Set<Long> timeSlotIds) {
}
