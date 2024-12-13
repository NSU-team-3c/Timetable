package ru.nsu.timetable.models.mappers;

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
        return new TimetableDTO(
                timetable.getId(),
                timetable.getEvents().stream()
                        .map(event -> {
                            System.out.println("Processing event: " + event.getId());
                            return eventMapper.toEventDTO(event);
                        })
                        .toList()
        );
    }
}
