package ru.nsu.timetable.models.dto;

import java.time.Instant;

public record RefreshTokenDTO(Long id, Long userId, String token, Instant expiryDate) {
}