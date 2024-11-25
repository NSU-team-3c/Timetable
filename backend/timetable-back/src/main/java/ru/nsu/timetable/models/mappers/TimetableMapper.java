package ru.nsu.timetable.models.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.TimetableDTO;
import ru.nsu.timetable.models.entities.Faculty;
import ru.nsu.timetable.models.entities.TimeSlot;
import ru.nsu.timetable.models.entities.Timetable;
import ru.nsu.timetable.repositories.FacultyRepository;
import ru.nsu.timetable.repositories.TimeSlotRepository;

@Component
public class TimetableMapper {
    private final FacultyRepository facultyRepository;
    private final TimeSlotRepository timeSlotRepository;

    public TimetableMapper(FacultyRepository facultyRepository, TimeSlotRepository timeSlotRepository) {
        this.facultyRepository = facultyRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimetableDTO toTimetableDTO(Timetable timetable) {
        return new TimetableDTO(timetable.getId(),
                timetable.getFaculties().stream()
                        .map(Faculty::getId)
                        .collect(Collectors.toSet()),
                timetable.getTimeSlots().stream()
                        .map(TimeSlot::getId)
                        .collect(Collectors.toSet()));
    }

    public Timetable toTimetable(TimetableDTO timetableDTO) {
        Timetable timetable = new Timetable();
        timetableDTO.facultyIds().forEach(facultyId ->
                facultyRepository.findById(facultyId).ifPresent(timetable::addFaculty)
        );
        timetableDTO.timeSlotIds().forEach(timeSlotId ->
                timeSlotRepository.findById(timeSlotId).ifPresent(timetable::addTimeSlot)
        );
        if (timetableDTO.id() != null) {
            timetable.setId(timetableDTO.id());
        }
        return timetable;
    }
}