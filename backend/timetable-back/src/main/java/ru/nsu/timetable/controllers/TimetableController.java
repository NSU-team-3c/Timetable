package ru.nsu.timetable.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.models.dto.*;
import ru.nsu.timetable.services.PrologIntegrationService;
import ru.nsu.timetable.services.RequirementsXmlGeneratorService;
import ru.nsu.timetable.services.TimetableService;
import ru.nsu.timetable.services.XmlParserService;

import ru.nsu.timetable.models.mappers.TimetableMapper;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/timetables")
@Tag(name = "Timetable controller")
public class TimetableController {

    private final TimetableService timetableService;

    @GetMapping("")
    public List<TimetableDTO> getAllTimetables() {
        return timetableService.getAllTimetables();
    }

    @GetMapping("/{id}")
    public TimetableDTO getTimetableById(@PathVariable Long id) {
        return timetableService.getTimetableById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTimetable(@PathVariable Long id) {
        timetableService.deleteTimetable(id);
    }

    @GetMapping("/generate")
    public ResponseEntity<TimetableDTO> generateAndSaveTimetable() {
        try {
            TimetableDTO timetableDTO = timetableService.generateAndSaveTimetable();
            return ResponseEntity.ok(timetableDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
