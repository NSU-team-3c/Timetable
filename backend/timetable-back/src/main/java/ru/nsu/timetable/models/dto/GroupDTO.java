package ru.nsu.timetable.models.dto;

import java.util.List;

public record GroupDTO(Long id, String number, List<String> students, List<Long> subjectIds) {
}
