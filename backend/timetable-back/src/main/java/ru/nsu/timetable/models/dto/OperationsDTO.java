package ru.nsu.timetable.models.dto;

import java.util.Date;

public record OperationsDTO(Long id, Date dateOfCreation, String description, String userAccount) {
}