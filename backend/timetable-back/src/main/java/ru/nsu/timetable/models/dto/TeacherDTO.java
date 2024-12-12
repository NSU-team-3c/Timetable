package ru.nsu.timetable.models.dto;

import java.util.List;

public record TeacherDTO(Long id, String organization, String education, String specialization,
                         List<Long> availableTimeSlotIds,
                         List<Long> subjectIds,
                         UserDTO user) {
}
