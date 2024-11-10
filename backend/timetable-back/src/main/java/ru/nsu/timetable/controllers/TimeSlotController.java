package ru.nsu.timetable.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.TimeSlotDTO;
import ru.nsu.timetable.models.entities.TimeSlot;
import ru.nsu.timetable.models.mappers.TimeSlotMapper;
import ru.nsu.timetable.services.TimeSlotService;

@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;
    private final TimeSlotMapper timeSlotMapper;

    @Autowired
    public TimeSlotController(TimeSlotService timeSlotService, TimeSlotMapper timeSlotMapper) {
        this.timeSlotService = timeSlotService;
        this.timeSlotMapper = timeSlotMapper;
    }

    @GetMapping
    public List<TimeSlotDTO> getAllTimeSlots() {
        return timeSlotService.getAllTimeSlots()
                .stream()
                .map(timeSlotMapper::toTimeSlotDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeSlotDTO> getTimeSlotById(@PathVariable Long id) {
        return timeSlotService.getTimeSlotById(id)
                .map(timeSlotMapper::toTimeSlotDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TimeSlotDTO> createTimeSlot(@RequestBody TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = timeSlotMapper.toTimeSlot(timeSlotDTO);
        TimeSlot savedTimeSlot = timeSlotService.saveTimeSlot(timeSlot);
        return ResponseEntity.created(URI.create("/timeslots/" + savedTimeSlot.getId()))
                .body(timeSlotMapper.toTimeSlotDTO(savedTimeSlot));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable Long id) {
        Optional<TimeSlot> timeSlot = timeSlotService.getTimeSlotById(id);
        if (timeSlot.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        timeSlotService.deleteTimeSlot(id);
        return ResponseEntity.noContent().build();
    }
}
