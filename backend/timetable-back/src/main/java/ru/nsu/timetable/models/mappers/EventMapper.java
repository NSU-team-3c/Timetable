package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.EventDTO;
import ru.nsu.timetable.models.entities.Event;

@Component
public class EventMapper {
    public EventDTO toEventDTO(Event event) {
        return new EventDTO(event.getId(), event.getStartTime(), event.getEndTime(), event.getTeacher().getName(),
                event.getSubject().getName(), event.getRoom().getName());
    }
}
