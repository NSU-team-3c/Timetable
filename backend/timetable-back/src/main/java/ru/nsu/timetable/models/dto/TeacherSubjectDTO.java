package ru.nsu.timetable.models.dto;

import java.util.List;

public record TeacherSubjectDTO(Long teacherId, List<Long> subjectIds) {
}
