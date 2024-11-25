package ru.nsu.timetable.payload.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @NotBlank
    @Email
    @Schema(description = "Email", example = "admin@gmail.com")
    @NotNull(message = "Email is required")
    private String email;

    @Schema(description = "Password", example = "test")
    @NotBlank(message = "Password is required")
    @NotNull(message = "Password cannot be null")
    private String password;
}
