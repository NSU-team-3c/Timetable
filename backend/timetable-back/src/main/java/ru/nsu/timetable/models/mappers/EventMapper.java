package ru.nsu.timetable.models.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.EventDTO;
import ru.nsu.timetable.models.entities.Event;
import ru.nsu.timetable.repositories.RoomRepository;
import ru.nsu.timetable.repositories.SubjectRepository;
import ru.nsu.timetable.services.UserService;

@Component
public class EventMapper {
    private final SubjectRepository subjectRepository;
    private final RoomRepository roomRepository;
    private final UserService userService;

    @Autowired
    public EventMapper(SubjectRepository subjectRepository,
                       RoomRepository roomRepository,
                       UserService userService) {
        this.subjectRepository = subjectRepository;
        this.roomRepository = roomRepository;
        this.userService = userService;
    }

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
