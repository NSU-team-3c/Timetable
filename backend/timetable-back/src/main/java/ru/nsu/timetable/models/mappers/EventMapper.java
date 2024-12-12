package ru.nsu.timetable.models.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.EventDTO;
import ru.nsu.timetable.models.entities.Event;
import ru.nsu.timetable.models.entities.Room;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.models.entities.Teacher;
import ru.nsu.timetable.repositories.RoomRepository;
import ru.nsu.timetable.repositories.SubjectRepository;
import ru.nsu.timetable.repositories.TeacherRepository;

@Component
public class EventMapper {
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public EventMapper(TeacherRepository teacherRepository,
                       SubjectRepository subjectRepository,
                       RoomRepository roomRepository) {
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.roomRepository = roomRepository;
    }

    public EventDTO toEventDTO(Event event) {
        return new EventDTO(event.getId(), event.getStartTime(), event.getEndTime(), event.getTeacher().getName(),
                event.getSubject().getName(), event.getRoom().getName());
    }

    public Event toEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setId(eventDTO.id());
        event.setStartTime(eventDTO.startTime());
        event.setEndTime(eventDTO.endTime());

        Teacher teacher = teacherRepository.findByName(eventDTO.teacherName())
                .orElseGet(() -> {
                    Teacher newTeacher = new Teacher();
                    newTeacher.setName(eventDTO.teacherName());
                    return teacherRepository.save(newTeacher);
                });
        event.setTeacher(teacher);

        Subject subject = subjectRepository.findByName(eventDTO.subjectName())
                .orElseGet(() -> {
                    Subject newSubject = new Subject();
                    newSubject.setName(eventDTO.subjectName());
                    return subjectRepository.save(newSubject);
                });
        event.setSubject(subject);

        Room room = roomRepository.findByName(eventDTO.roomName())
                .orElseGet(() -> {
                    Room newRoom = new Room();
                    newRoom.setName(eventDTO.roomName());
                    return roomRepository.save(newRoom);
                });
        event.setRoom(room);

        return event;
    }
}
