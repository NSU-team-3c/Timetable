package ru.nsu.timetable.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.configuration.security.jwt.JwtUtils;
import ru.nsu.timetable.exceptions.UnauthorizedException;
import ru.nsu.timetable.models.dto.*;
import ru.nsu.timetable.services.TimetableService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/timetables")
@Tag(name = "Timetable controller")
public class TimetableController {
    private final TimetableService timetableService;
    private final JwtUtils jwtUtils;

    @GetMapping("")
    @Operation(summary = "Get timetable for particular user", security = @SecurityRequirement(name = "bearerAuth"))
    public TimetableDTO getTimetableForUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String email = jwtUtils.getUserNameFromJwtToken(token);
            return timetableService.getTimetableForUser(email);
        }
        throw new UnauthorizedException("Invalid or missing token");
    }

    @GetMapping("/generate")
    public ResponseEntity<TimetableDTO> generateAndSaveTimetable() {
        TimetableDTO timetableDTO = timetableService.generateAndSaveTimetable();
        return ResponseEntity.ok(timetableDTO);
    }
}
