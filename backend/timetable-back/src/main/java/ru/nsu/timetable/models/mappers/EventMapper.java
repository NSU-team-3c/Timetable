package ru.nsu.timetable.models.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.EventDTO;
import ru.nsu.timetable.models.entities.Event;
import ru.nsu.timetable.repositories.RoomRepository;
import ru.nsu.timetable.repositories.SubjectRepository;
import ru.nsu.timetable.repositories.TeacherRepository;
import ru.nsu.timetable.services.UserService;

@Component
public class EventMapper {
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final RoomRepository roomRepository;
    private final UserService userService;

    @Autowired
    public EventMapper(TeacherRepository teacherRepository,
                       SubjectRepository subjectRepository,
                       RoomRepository roomRepository,
                       UserService userService) {
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.roomRepository = roomRepository;
        this.userService = userService;
    }

    public EventDTO toEventDTO(Event event) {
        String teacherName = (event.getTeacher() != null && event.getTeacher().getUserData() != null)
                ? event.getTeacher().getUserData().getSurname() +
                event.getTeacher().getUserData().getName()
                : "Unknown Teacher";

        String subjectName = event.getSubject() != null ? event.getSubject().getName() : "Unknown Subject";
        String roomName = event.getRoom() != null ? event.getRoom().getName() : "Unknown Room";

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
