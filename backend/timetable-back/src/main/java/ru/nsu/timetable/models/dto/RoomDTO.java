package ru.nsu.timetable.models.dto;

public record RoomDTO(
        Long id,
        String number,
        Integer capacity,
        String type) {
}
