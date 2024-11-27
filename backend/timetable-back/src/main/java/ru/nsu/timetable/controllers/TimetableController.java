package ru.nsu.timetable.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.TimetableDTO;
import ru.nsu.timetable.services.TimetableService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/management")
@Tag(name = "Timetable controller")
public class TimetableController {
    private final TimetableService timetableService;

    @GetMapping("/timetables")
    public List<TimetableDTO> getAllTimetables() {
        return timetableService.getAllTimetables();
    }

    @GetMapping("/timetables/{id}")
    public TimetableDTO getTimetableById(@PathVariable Long id) {
        return timetableService.getTimetableById(id);
    }

    @PostMapping("/timetables")
    public TimetableDTO createTimetable(@RequestBody TimetableDTO timetableDTO) {
        return timetableService.saveTimetable(timetableDTO);
    }

    @DeleteMapping("/timetables/{id}")
    public void deleteTimetable(@PathVariable Long id) {
        timetableService.deleteTimetable(id);
    }
}