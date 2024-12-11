package ru.nsu.timetable.models.dto;

public record SubjectRequestDTO(String name, String code, String description,
                                Integer duration, String audienceType) {
}
