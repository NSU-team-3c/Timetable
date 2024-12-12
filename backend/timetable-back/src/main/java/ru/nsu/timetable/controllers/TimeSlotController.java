package ru.nsu.timetable.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.TimeSlotDTO;
import ru.nsu.timetable.services.TimeSlotService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/timeslots")
@Tag(name = "TimeSlot controller")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;

    @GetMapping("")
    public List<TimeSlotDTO> getAllTimeSlots() {
        return timeSlotService.getAllTimeSlots();
    }

    @GetMapping("/{id}")
    public TimeSlotDTO getTimeSlotById(@PathVariable Long id) {
        return timeSlotService.getTimeSlotById(id);
    }

    @PostMapping("")
    public TimeSlotDTO createTimeSlot(@RequestBody TimeSlotDTO timeSlotDTO) {
        return timeSlotService.saveTimeSlot(timeSlotDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTimeSlot(@PathVariable Long id) {
        timeSlotService.deleteTimeSlot(id);
    }

    @PutMapping("/{id}")
    public TimeSlotDTO updateTimeSlot(@PathVariable long id, @RequestBody TimeSlotDTO timeSlotDTO) {
        return timeSlotService.updateTimeSlot(id, timeSlotDTO);
    }
}
