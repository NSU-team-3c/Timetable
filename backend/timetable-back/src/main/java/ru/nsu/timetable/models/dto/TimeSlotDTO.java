package ru.nsu.timetable.models.dto;

import java.util.Date;

public record TimeSlotDTO(Long id, Date startTime, Date endTime, Long subjectId, Long roomId, Long teacherId) {
}
