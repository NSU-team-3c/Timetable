package ru.nsu.timetable.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.exceptions.InvalidDataException;
import ru.nsu.timetable.models.dto.UserRegisterDTO;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.models.mappers.UserMapper;
import ru.nsu.timetable.payload.requests.*;
import ru.nsu.timetable.payload.response.JwtAuthResponse;
import ru.nsu.timetable.payload.response.MessageResponse;
import ru.nsu.timetable.services.AuthService;
import ru.nsu.timetable.services.UserService;

import jakarta.validation.Valid;
import ru.nsu.timetable.sockets.MessageUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/admin")
@Tag(name = "Admin controller")
@RequiredArgsConstructor
public class AdminController {

    private static final String EMAIL_EXISTS_ERROR = "Error: email already exists";

    private final UserService userService;
    private final UserMapper userMapper;
    private final MessageUtils messageUtils;
    private final AuthService authService;

    @Operation(summary = "Registration of new student account in system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student account created successfully",
                    content = {@Content(schema = @Schema(implementation = UserRegisterDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Email already exists",
                    content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = @Content)
    })
    @PostMapping("/register_student")
    @Transactional
    @Tag(name = "Student Registration")
    public JwtAuthResponse registerNewStudent(@Valid @RequestBody RegistrationRequest registrationRequest) {
        User user = registerUser(registrationRequest, "STUDENT");
        messageUtils.sendMessage(null, "registration", "New student " + user.getEmail() + " registered", null);
        return authService.authenticate(new AuthRequest(registrationRequest.getEmail(), registrationRequest.getPassword()));
    }

    @Operation(summary = "Registration of new teacher account in system", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher account created successfully",
                    content = {@Content(schema = @Schema(implementation = UserRegisterDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Email already exists",
                    content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = @Content)
    })
    @PostMapping("/register_teacher")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional
    @Tag(name = "Teacher Registration")
    public UserRegisterDTO registerNewTeacher(HttpServletRequest request,  @Valid @RequestBody RegistrationRequest registrationRequest) {
        User user = registerUser(registrationRequest, "TEACHER");
        messageUtils.sendMessage(request, "registration", "New teacher " + user.getEmail() + " registered", null);
        return userMapper.toUserRegisterDTO(user);
    }

    @Operation(summary = "Registration of new admin account in system", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin account created successfully",
                    content = {@Content(schema = @Schema(implementation = UserRegisterDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Email already exists",
                    content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = @Content)
    })
    @PostMapping("/register_admin")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Transactional
    @Tag(name = "Admin Registration")
    public UserRegisterDTO registerNewAdmin(HttpServletRequest request, @Valid @RequestBody RegistrationRequest registrationRequest) {
        User user = registerUser(registrationRequest, "ADMINISTRATOR");
        messageUtils.sendMessage(request, "registration", "New admin " + user.getEmail() + " registered", null);
        return userMapper.toUserRegisterDTO(user);
    }

    private User registerUser(RegistrationRequest request, String role) {
        String email = request.getEmail();
        String password = request.getPassword();
        String name = request.getName();
        String surname = request.getSurname();

        if (email == null || email.isEmpty()) {
            throw new InvalidDataException("Error: email is required");
        }
        if (password == null || password.isEmpty()) {
            throw new InvalidDataException("Error: password is required");
        }
        if (userService.existByEmailCheck(email)) {
            throw new InvalidDataException(EMAIL_EXISTS_ERROR);
        }

        User newUser;
        switch (role) {
            case "STUDENT" -> newUser = userService.saveNewUser(email, password, name, surname);
            case "TEACHER" -> newUser = userService.saveNewTeacher(email, password, name, surname);
            case "ADMINISTRATOR" -> newUser = userService.saveNewAdmin(email, password, name, surname);
            default -> throw new InvalidDataException("Error: Invalid role - " + role);
        }

        return newUser;
    }
}
