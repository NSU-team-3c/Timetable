package ru.nsu.timetable.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.configuration.security.jwt.JwtUtils;
import ru.nsu.timetable.models.dto.*;
import ru.nsu.timetable.services.TimetableService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/timetables")
@Tag(name = "Timetable controller")
public class TimetableController {

    private final TimetableService timetableService;
    private final JwtUtils jwtUtils;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("")
    @Operation(summary = "Get timetable for particular user", security = @SecurityRequirement(name = "bearerAuth"))
    public TimetableDTO getTimetableForUser(HttpServletRequest request) {
        String email = jwtUtils.getEmailFromHeader(request);
        return timetableService.getTimetableForUser(email);
    }

    @GetMapping("/generate")
    public ResponseEntity<TimetableDTO> generateAndSaveTimetable() {
        TimetableDTO timetableDTO = timetableService.generateAndSaveTimetable();

        messagingTemplate.convertAndSend("/websockets/notifications/newLog", "Расписание обновлено");

        return ResponseEntity.ok(timetableDTO);
    }
}
