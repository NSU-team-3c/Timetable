package ru.nsu.timetable.services;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.InvalidDataException;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.exceptions.TimetableGenerationException;
import ru.nsu.timetable.exceptions.TimetableParsingException;
import ru.nsu.timetable.models.constants.ERole;
import ru.nsu.timetable.models.dto.TimetableDTO;
import ru.nsu.timetable.models.entities.*;
import ru.nsu.timetable.models.mappers.TimetableMapper;
import ru.nsu.timetable.repositories.*;

@RequiredArgsConstructor
@Service
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final TimetableMapper timetableMapper;
    private final RequirementsXmlGeneratorService requirementsXmlGeneratorService;
    private final PrologIntegrationService prologIntegrationService;
    private final XmlParserService xmlParserService;
    private final GroupRepository groupRepository;
    private final RoomRepository roomRepository;
    private final EventRepository eventRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public TimetableDTO getTimetableForUser(String email) {
        User user = userService.getUserByEmail(email);
        if (user.getRoles().stream().map(Role::getName).anyMatch(eRole -> eRole.equals(ERole.ROLE_ADMINISTRATOR))) {
            Timetable fullTimetable = getGeneratedTimetable();
            return timetableMapper.toTimetableDTO(fullTimetable);
        }
        if (user.getRoles().stream().map(Role::getName).anyMatch(eRole -> eRole.equals(ERole.ROLE_USER))) {
            return timetableMapper.toTimetableDTO(getTimetableForStudent(user));
        } else {
            return timetableMapper.toTimetableDTO(getTimetableForTeacher(user));
        }
    }

    private Timetable getGeneratedTimetable() {
        return timetableRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find generated timetable."));
    }

    private Timetable getTimetableForStudent(User user) {
        Group group = user.getGroup();
        if (group == null) {
            throw new InvalidDataException("Group not found for user " + user.getEmail());
        }
        Timetable fullTimetable = getGeneratedTimetable();
        List<Event> events = fullTimetable.getEvents()
                .stream()
                .filter(event -> {
                    for (Group groupAtEvent : event.getGroups()) {
                        if (groupAtEvent.getId() == group.getId()) {
                            return true;
                        }
                    }
                    return false;
                })
                .toList();

        return Timetable.builder()
                .events(events)
                .build();
    }

    private Timetable getTimetableForTeacher(User user) {
        Timetable fullTimetable = getGeneratedTimetable();
        List<Event> events = fullTimetable.getEvents()
                .stream()
                .filter(event -> event.getTeacher().getId().equals(user.getId()))
                .toList();

        return Timetable.builder()
                .events(events)
                .build();
    }

    public TimetableDTO generateAndSaveTimetable() {
        try {
            List<Group> groups = groupRepository.findAll();
            List<Room> rooms = roomRepository.findAll();
            List<User> teachers = userRepository.findAll()
                    .stream()
                    .filter(user -> user.getRoles().stream().map(Role::getName).anyMatch(eRole -> eRole.equals(ERole.ROLE_TEACHER)))
                    .toList();

            String requirementsFilePath = "../../prolog/v2/reqs.xml";
            requirementsXmlGeneratorService.generateXml(groups, rooms, teachers, 42, 7, requirementsFilePath);

            String outputFilePath = prologIntegrationService.generateTimetable(requirementsFilePath, "run");

            if (outputFilePath == null || outputFilePath.isEmpty()) {
                throw new TimetableGenerationException("Failed to generate timetable. No output file returned");
            }

            Timetable timetable = xmlParserService.parseTimetable(outputFilePath);

            if (timetable == null) {
                throw new TimetableParsingException("Failed to parse timetable");
            }

            Timetable savedTimetable = timetableRepository.save(timetable);

            for (Event event : timetable.getEvents()) {
                event.setTimetable(savedTimetable);
            }
            eventRepository.saveAll(timetable.getEvents());

            return timetableMapper.toTimetableDTO(savedTimetable);

        } catch (IOException | InterruptedException e) {
            throw new TimetableGenerationException("Error occurred during timetable generation", e);
        }
    }
}
