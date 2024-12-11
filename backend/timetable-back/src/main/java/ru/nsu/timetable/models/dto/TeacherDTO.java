package ru.nsu.timetable.models.dto;

import java.util.List;
import java.util.Set;

public record TeacherDTO(Long id, String name, List<TimeSlotDTO> availableTimeSlots,
                         String qualification, long userId, Set<Integer> freeDays) {
}
