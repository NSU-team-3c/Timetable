package ru.nsu.timetable.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(
            UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Registration of new student account in system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Information about password that is automatically linked to generated account is returned", content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Email already exists", content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = @Content)})
    @PostMapping("/register_student")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional
    public ResponseEntity<?> registerNewStudent(@Valid @RequestBody RegistrationRequest registrationRequest) {
        String newUserEmail = registrationRequest.getEmail();

        if (newUserEmail != null && !newUserEmail.isEmpty()) {
            if (userService.existByEmailCheck(newUserEmail)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: email already exists"));
            }
        }
        String description = userService.saveNewUser(newUserEmail, registrationRequest.getFullName(),
                registrationRequest.getPhone());

        return ResponseEntity.ok(new MessageResponse("New user created successfully"));
    }

    @Operation(
            summary = "Registration of new teacher account in system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Information about password that is automatically linked to generated account is returned", content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Email already exists", content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = @Content)})
    @PostMapping("/register_teacher")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional
    public ResponseEntity<?> registerNewTeacher(@Valid @RequestBody RegistrationRequest registrationRequest) {
        String newUserEmail = registrationRequest.getEmail();

        if (newUserEmail != null && !newUserEmail.isEmpty()) {
            if (userService.existByEmailCheck(newUserEmail)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: email already exists"));
            }
        }
        String description = userService.saveNewTeacher(newUserEmail, registrationRequest.getFullName(),
                registrationRequest.getPhone());

        return ResponseEntity.ok(new MessageResponse("New teacher created successfully"));
    }

    @Operation(
            summary = "Registration of new admin account in system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Information about password that is automatically linked to generated account is returned", content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Email already exists", content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = @Content)})
    @PostMapping("/register_admin")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional
    public ResponseEntity<?> registerNewAdmin(@Valid @RequestBody RegistrationRequest registrationRequest) {
        String newUserEmail = registrationRequest.getEmail();

        if (newUserEmail != null && !newUserEmail.isEmpty()) {
            if (userService.existByEmailCheck(newUserEmail)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: email already exists"));
            }
        }
        String description = userService.saveNewAdmin(newUserEmail, registrationRequest.getFullName(),
                registrationRequest.getPhone());

        return ResponseEntity.ok(new MessageResponse("New admin created successfully"));
    }
}
