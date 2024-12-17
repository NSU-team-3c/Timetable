package ru.nsu.timetable.models.dto;

import java.util.Date;

public record UserDTO(
        String email,
        String phone,
        String role,
        String surname,
        String name,
        String patronymic,
        Date birthday,
        String about,
        String photoUrl,
        String group
) {}
