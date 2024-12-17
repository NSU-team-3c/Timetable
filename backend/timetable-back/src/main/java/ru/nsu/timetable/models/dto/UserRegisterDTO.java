package ru.nsu.timetable.models.dto;

public record UserRegisterDTO(
        Long id,
        String name,
        String surname,
        String email,
        String role
) {}