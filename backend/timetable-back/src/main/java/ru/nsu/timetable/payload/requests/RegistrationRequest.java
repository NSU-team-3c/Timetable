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

    @NotBlank
    @Schema(description = "Username", example = "user007")
    private String username;

    @NotBlank
    @Schema(description = "Full name", example = "Surname Name Patronymic")
    private String fullName;

    @Schema(description = "Password", example = "12345")
    @NotBlank
    private String password;

    @Schema(description = "Phone number", example = "00000000000")
    private String phone;
}
