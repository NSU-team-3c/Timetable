package ru.nsu.timetable.models.mappers;

import java.util.List;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.EventDTO;
import ru.nsu.timetable.models.dto.GeneratedTimetableDTO;
import ru.nsu.timetable.models.dto.TimetableDTO;
import ru.nsu.timetable.models.dto.UnplacedSubjectDTO;
import ru.nsu.timetable.models.entities.Timetable;
import ru.nsu.timetable.models.entities.UnplacedSubject;

@Component
public class TimetableMapper {
    private final EventMapper eventMapper;

    public TimetableMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public TimetableDTO toTimetableDTO(Timetable timetable) {
        return new TimetableDTO(timetableToEventDtoList(timetable));
    }

    public GeneratedTimetableDTO toSuccessfullyGeneratedTimetableDTO(Timetable timetable) {
        return new GeneratedTimetableDTO(
                true,
                timetableToEventDtoList(timetable),
                null
        );
    }

    public GeneratedTimetableDTO toUnsuccessfullyGeneratedTimetableDTO(List<UnplacedSubject> unplacedSubjects) {
        return new GeneratedTimetableDTO(
                false,
                null,
                unplacedSubjects.stream()
                        .map(this::toUnplacedSubjectDTO)
                        .toList()
        );
    }

    private List<EventDTO> timetableToEventDtoList(Timetable timetable) {
        return timetable.getEvents().stream()
                .map(event -> {
                    System.out.println("Processing event: " + event.getId());
                    return eventMapper.toEventDTO(event);
                })
                .toList();
    }

    private UnplacedSubjectDTO toUnplacedSubjectDTO(UnplacedSubject unplacedSubject) {
        String teacherName = unplacedSubject.getTeacher().getSurname() + " " + unplacedSubject.getTeacher().getName();
        String groupNumber = unplacedSubject.getGroup().getNumber();
        String subjectName = unplacedSubject.getSubject().getName();
        String audienceType = unplacedSubject.getAudienceType() != null
                ? unplacedSubject.getAudienceType().name().toLowerCase()
                : null;

        return new UnplacedSubjectDTO(
                groupNumber,
                subjectName,
                teacherName,
                audienceType
        );
    }
}
