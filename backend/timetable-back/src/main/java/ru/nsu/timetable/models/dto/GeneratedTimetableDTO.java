package ru.nsu.timetable.models.dto;

import java.util.List;

public record GeneratedTimetableDTO(boolean isGeneratedSuccessfully, List<EventDTO> events, List<UnplacedSubjectDTO> unplacedSubjects) {
}
