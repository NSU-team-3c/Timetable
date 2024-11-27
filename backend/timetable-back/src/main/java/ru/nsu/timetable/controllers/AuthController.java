package ru.nsu.timetable.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.payload.requests.AuthRequest;
import ru.nsu.timetable.payload.response.JwtAuthResponse;
import ru.nsu.timetable.payload.response.MessageResponse;
import ru.nsu.timetable.services.AuthService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Auth controller", description = "Authorization controller where person can get jwt and refresh token")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Request for account authorization",
            description = """
                    User's data is checked and jwt with refresh token is issued""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = JwtAuthResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "There is no user with such email", content = {@Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = @Content)})
    @PostMapping("/auth")
    public JwtAuthResponse auth(@Valid @RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }
}
