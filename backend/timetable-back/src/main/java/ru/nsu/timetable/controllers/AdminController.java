package ru.nsu.timetable.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.payload.requests.*;
import ru.nsu.timetable.payload.response.MessageResponse;
import ru.nsu.timetable.services.UserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/admin")
@Tag(name = "Admin controller")
@RequiredArgsConstructor
public class AdminController {

    private static final String EMAIL_EXISTS_ERROR = "Error: email already exists";
    private static final String SUCCESS_MESSAGE = "User created successfully";

    private final UserService userService;


    @Operation(summary = "Registration of new student account in system", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student account created successfully",
                    content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Email already exists",
                    content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = @Content)
    })
    @PostMapping("/register_student")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional
    @Tag(name = "Student Registration")
    public ResponseEntity<?> registerNewStudent(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return registerUser(registrationRequest, "STUDENT");
    }

    @Operation(summary = "Registration of new teacher account in system", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher account created successfully",
                    content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Email already exists",
                    content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = @Content)
    })
    @PostMapping("/register_teacher")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional
    @Tag(name = "Teacher Registration")
    public ResponseEntity<?> registerNewTeacher(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return registerUser(registrationRequest, "TEACHER");
    }

    @Operation(summary = "Registration of new admin account in system", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin account created successfully",
                    content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Email already exists",
                    content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = @Content)
    })
    @PostMapping("/register_admin")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional
    @Tag(name = "Admin Registration")
    public ResponseEntity<?> registerNewAdmin(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return registerUser(registrationRequest, "ADMINISTRATOR");
    }

    private ResponseEntity<?> registerUser(RegistrationRequest request, String role) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: email is required"));
        }
        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: password is required"));
        }
        if (userService.existByEmailCheck(email)) {
            return ResponseEntity.badRequest().body(new MessageResponse(EMAIL_EXISTS_ERROR));
        }

        switch (role) {
            case "STUDENT" -> userService.saveNewUser(email, request.getFullName(), request.getPhone(), password);
            case "TEACHER" -> userService.saveNewTeacher(email, request.getFullName(), request.getPhone(), password);
            case "ADMINISTRATOR" ->
                    userService.saveNewAdmin(email, request.getFullName(), request.getPhone(), password);
            default -> throw new IllegalArgumentException("Invalid role");
        }

        return ResponseEntity.ok(new MessageResponse(SUCCESS_MESSAGE));
    }
}
