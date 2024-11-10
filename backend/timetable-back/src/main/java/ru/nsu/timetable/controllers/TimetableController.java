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
import ru.nsu.timetable.models.dto.TimetableDTO;
import ru.nsu.timetable.models.entities.Timetable;
import ru.nsu.timetable.models.mappers.TimetableMapper;
import ru.nsu.timetable.services.TimetableService;

@RestController
@RequestMapping("/timetables")
public class TimetableController {
    private final TimetableService timetableService;
    private final TimetableMapper timetableMapper;

    @Autowired
    public TimetableController(TimetableService timetableService, TimetableMapper timetableMapper) {
        this.timetableService = timetableService;
        this.timetableMapper = timetableMapper;
    }

    @GetMapping
    public List<TimetableDTO> getAllTimetables() {
        return timetableService.getAllTimetables()
                .stream()
                .map(timetableMapper::toTimetableDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimetableDTO> getTimetableById(@PathVariable Long id) {
        return timetableService.getTimetableById(id)
                .map(timetableMapper::toTimetableDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TimetableDTO> createTimetable(@RequestBody TimetableDTO timetableDTO) {
        Timetable timetable = timetableMapper.toTimetable(timetableDTO);
        Timetable savedTimetable = timetableService.saveTimetable(timetable);
        return ResponseEntity.created(URI.create("/timetables/" + savedTimetable.getId()))
                .body(timetableMapper.toTimetableDTO(savedTimetable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimetable(@PathVariable Long id) {
        Optional<Timetable> timetable = timetableService.getTimetableById(id);
        if (timetable.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        timetableService.deleteTimetable(id);
        return ResponseEntity.noContent().build();
    }
}