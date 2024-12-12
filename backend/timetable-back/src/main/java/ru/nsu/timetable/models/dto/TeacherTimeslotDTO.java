package ru.nsu.timetable.models.dto;

import java.util.List;

public record TeacherTimeslotDTO(Long teacherId, List<Long> timeSlotIds) {
}
