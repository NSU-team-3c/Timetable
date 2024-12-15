package ru.nsu.timetable.payload.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank
    @Schema(description = "Email", example = "admin@mail.ru")
    private String email;

    @Schema(description = "Password", example = "12345")
    @NotBlank
    private String password;
}
