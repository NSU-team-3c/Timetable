package ru.nsu.timetable.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.TimetableDTO;
import ru.nsu.timetable.models.entities.Timetable;
import ru.nsu.timetable.models.mappers.TimetableMapper;
import ru.nsu.timetable.repositories.TimetableRepository;

@RequiredArgsConstructor
@Service
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final TimetableMapper timetableMapper;

    public List<TimetableDTO> getAllTimetables() {
        return timetableRepository
                .findAll()
                .stream()
                .map(timetableMapper::toTimetableDTO)
                .toList();
    }

    public TimetableDTO getTimetableById(long id) {
        return timetableMapper.toTimetableDTO(getTimetable(id));
    }

    public Timetable saveTimetable(Timetable timetable) {
        return timetableRepository.save(timetable);
    }

    public void deleteTimetable(Long id) {
        if (timetableRepository.existsById(id)) {
            timetableRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Timetable with id " + id + " not found");
        }
    }

    private Timetable getTimetable(Long id) {
        Optional<Timetable> timetable = timetableRepository.findById(id);
        if (timetable.isEmpty()) {
            throw new ResourceNotFoundException("Timetable with id " + id + " not found");
        } else {
            return timetable.get();
        }
    }
}
