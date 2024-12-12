package ru.nsu.timetable.models.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.TimetableDTO;
import ru.nsu.timetable.models.entities.Timetable;

@Component
public class TimetableMapper {
    private final EventMapper eventMapper;

    public TimetableMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public TimetableDTO toTimetableDTO(Timetable timetable) {
        return new TimetableDTO(timetable.getId(),
                timetable.getEvents().stream()
                        .map(eventMapper::toEventDTO)
                        .collect(Collectors.toList()));
    }

    public Timetable toTimetable(TimetableDTO timetableDTO) {
        Timetable timetable = new Timetable();
        timetable.setId(timetableDTO.id());
        timetable.setEvents(
                timetableDTO.events().stream()
                        .map(eventMapper::toEvent)
                        .collect(Collectors.toList())
        );
        return timetable;
    }
}
