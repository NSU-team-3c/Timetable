package ru.nsu.timetable.models.dto;

import java.util.Set;

public record TeacherDTO(Long id, String name, Set<Long> occupiedTimeSlotsIds,
                         String qualification, long userId, Set<Integer> freeDays) {
}
