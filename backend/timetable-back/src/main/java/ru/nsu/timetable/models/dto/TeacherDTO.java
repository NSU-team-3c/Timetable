package ru.nsu.timetable.models.dto;

import java.util.List;

public record TeacherDTO(Long id, String name, String organization, String education, String specialization,
                         List<TimeSlotDTO> availableTimeSlots,
                         List<Long> subjectIds,
                         Long userId) {
}
