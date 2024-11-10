package ru.nsu.timetable.models.dto;

import java.util.Set;

public record RoomDTO(Long id, String name, Integer capacity, String type, Set<Long> occupiedTimeSlotsIds) {
}
