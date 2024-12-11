package ru.nsu.timetable.models.dto;

public record TeacherRequestDTO(String name, String organization, String education, String specialization, Long userId) {
}
