package ru.nsu.timetable.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.configuration.security.jwt.JwtUtils;
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.dto.UserDTO;
import ru.nsu.timetable.models.dto.UserInputDTO;
import ru.nsu.timetable.services.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User controller")
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @GetMapping("")
    @Operation(summary = "Get user info", security = @SecurityRequirement(name = "bearerAuth"))
    public UserDTO getUser(HttpServletRequest request) {
        String email = jwtUtils.getEmailFromHeader(request);
        return userService.getUserInfoByEmail(email);
    }

    @PutMapping("")
    @Operation(summary = "Update user info (если группа не определена передавайте null!!!)", security = @SecurityRequirement(name = "bearerAuth"))
    public UserDTO updateUser(@RequestBody UserInputDTO userInputDTO, HttpServletRequest request) {
        String email = jwtUtils.getEmailFromHeader(request);
        return userService.updateUserByEmail(email, userInputDTO);
    }

    @GetMapping("/teachers")
    @Operation(summary = "Get all users with role teacher")
    public List<TeacherDTO> getTeachers() {
        return userService.getTeachers();
    }
}
