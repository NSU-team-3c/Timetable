package ru.nsu.timetable.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.configuration.security.jwt.JwtUtils;
import ru.nsu.timetable.models.dto.TimeSlotDTO;
import ru.nsu.timetable.models.entities.TimeSlot;
import ru.nsu.timetable.services.TimeSlotService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/timeslots")
@Tag(name = "TimeSlot controller")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;
    private final JwtUtils jwtUtils;

    @GetMapping("")
    @Operation(summary = "Get availability for teacher", security = @SecurityRequirement(name = "bearerAuth"))
    public List<TimeSlot> getAllTimeSlotsFrTeacher(HttpServletRequest request) {
        String email = jwtUtils.getEmailFromHeader(request);
        return timeSlotService.getAllTimeSlotsForTeacher(email);
    }

    @PostMapping("")
    @Operation(summary = "Create new availability timeslots for teacher. All current teacher time slots are returned.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public List<TimeSlot> createTimeSlots(HttpServletRequest request, @RequestBody List<TimeSlotDTO> timeSlotDTOs) {
        String email = jwtUtils.getEmailFromHeader(request);
        return timeSlotService.saveTimeSlots(email, timeSlotDTOs);
    }

    @Operation(summary = "Delete timeslots by ids for teacher. All current teacher time slots are returned.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("")
    public List<TimeSlot> deleteTimeSlots(HttpServletRequest request, @RequestBody List<Long> timeSlotIds) {
        String email = jwtUtils.getEmailFromHeader(request);
        return timeSlotService.deleteTimeSlots(email, timeSlotIds);
    }
}
