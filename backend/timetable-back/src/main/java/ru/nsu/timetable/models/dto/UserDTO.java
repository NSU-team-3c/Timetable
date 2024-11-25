package ru.nsu.timetable.models.dto;

import java.util.Set;

public record UserDTO(
        Long id,
        String username,
        String role,
        String email,
        String phone,
        String fullName,
        Set<String> roles
) {}
