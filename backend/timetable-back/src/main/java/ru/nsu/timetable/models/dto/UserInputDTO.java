package ru.nsu.timetable.models.dto;

import java.util.Date;

public record UserInputDTO(
        String phone,
        String surname,
        String name,
        String patronymic,
        Date birthday,
        String about,
        String photoUrl,
        String group
) {}
