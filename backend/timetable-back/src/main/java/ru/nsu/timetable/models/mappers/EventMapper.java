package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.EventDTO;
import ru.nsu.timetable.models.entities.Event;

@Component
public class EventMapper {
    public EventDTO toEventDTO(Event event) {
        String teacherName = event.getTeacher().getSurname() +
                event.getTeacher().getName();

        String subjectName = event.getSubject() != null ? event.getSubject().getName() : "Unknown Subject";
        String roomName = event.getRoom() != null ? event.getRoom().getNumber() : "Unknown Room";

        return new EventDTO(
                event.getId(),
                event.getStartTime(),
                event.getEndTime(),
                teacherName,
                subjectName,
                roomName
        );
    }
}
