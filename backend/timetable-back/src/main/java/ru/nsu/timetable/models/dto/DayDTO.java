package ru.nsu.timetable.models.dto;

import java.util.Set;

public record DayDTO(int dayNumber, String dayName, Set<TimeSlotDTO> timeSlots) {}