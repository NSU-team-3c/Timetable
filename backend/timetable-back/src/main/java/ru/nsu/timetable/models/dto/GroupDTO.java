package ru.nsu.timetable.models.dto;

import java.util.List;

public record GroupDTO(Long id, String name, List<String> students, List<Long> subjectIds) {
}
