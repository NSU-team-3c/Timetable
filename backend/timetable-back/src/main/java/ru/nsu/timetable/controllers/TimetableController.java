package ru.nsu.timetable.controllers;

import java.time.Instant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.configuration.security.jwt.JwtUtils;
import ru.nsu.timetable.exceptions.GenerationInProgressException;
import ru.nsu.timetable.models.dto.*;
import ru.nsu.timetable.services.TimetableService;
import ru.nsu.timetable.sockets.MessageUtils;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/timetables")
@Tag(name = "Timetable controller")
public class TimetableController {
    private volatile boolean isGenerating = false;
    private final Object lock = new Object();
    private Instant lastStartGenerationTime;

    private final TimetableService timetableService;
    private final JwtUtils jwtUtils;
    private final MessageUtils messageUtils;

    @GetMapping("")
    @Operation(summary = "Get timetable for particular user", security = @SecurityRequirement(name = "bearerAuth"))
    public TimetableDTO getTimetableForUser(HttpServletRequest request) {
        String email = jwtUtils.getEmailFromHeader(request);
        return timetableService.getTimetableForUser(email);
    }

    @GetMapping("/generate")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Generate timetable", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<GeneratedTimetableDTO> generateAndSaveTimetable(HttpServletRequest request) {
        synchronized (lock) {
            if (isGenerating) {
                throw new GenerationInProgressException("The generation is already running. Try again later.");
            }
            isGenerating = true;
        }
        messageUtils.sendMessage(request, "generation", "started", null);
        try {
            lastStartGenerationTime = Instant.now();
            GeneratedTimetableDTO generatedTimetableDTO = timetableService.generateAndSaveTimetable();
            return ResponseEntity.ok(generatedTimetableDTO );
        } finally {
            isGenerating = false;
            //todo: тут вернуть в subMessage минимальное неудовлетворяемое множество, если есть
            messageUtils.sendMessage(request, "generation", "finished", null);
        }
    }

    @GetMapping("/generating_status")
    public ResponseEntity<GenerationStatusDTO> getGenerationStatus() {
        return ResponseEntity.ok(new GenerationStatusDTO(isGenerating, lastStartGenerationTime));
    }
}
