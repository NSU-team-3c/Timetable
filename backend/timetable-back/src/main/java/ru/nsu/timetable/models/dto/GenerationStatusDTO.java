package ru.nsu.timetable.models.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

public record GenerationStatusDTO(
        boolean isGenerating,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
        Instant lastStartGenerationTime
) {
}
