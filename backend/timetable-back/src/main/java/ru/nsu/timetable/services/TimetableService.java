package ru.nsu.timetable.services;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.InvalidDataException;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.constants.ERole;
import ru.nsu.timetable.models.dto.TimetableDTO;
import ru.nsu.timetable.models.entities.*;
import ru.nsu.timetable.models.mappers.TimetableMapper;
import ru.nsu.timetable.repositories.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

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
            throw new InvalidDataException("Cannot find timetable for user " + user.getEmail() +
                    " because the group has not been identified");
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

        Timetable timetableForStudent = new Timetable();
        timetableForStudent.setEvents(events);
        return timetableForStudent;
    }

    private Timetable getTimetableForTeacher(User user) {
        Timetable fullTimetable = getGeneratedTimetable();
        List<Event> events = fullTimetable.getEvents()
                .stream()
                .filter(event -> event.getTeacher().getId() == user.getId())
                .toList();

        Timetable timetableForTeacher = new Timetable();
        timetableForTeacher.setEvents(events);
        return timetableForTeacher;
    }

    public TimetableDTO generateAndSaveTimetable() throws IOException, InterruptedException, ParserConfigurationException, TransformerException {
        List<Group> groups = groupRepository.findAll();
        List<Room> rooms = roomRepository.findAll();
        List<User> teachers = userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles().stream().map(Role::getName).anyMatch(eRole -> eRole.equals(ERole.ROLE_ADMINISTRATOR)))
                .toList();

        System.out.println("Fetched " + groups.size() + " groups, " + rooms.size() + " rooms, " + teachers.size() + " teachers.");

        String requirementsFilePath = "prolog/v2/reqs2.xml";

        requirementsXmlGeneratorService.generateXml(groups, rooms, teachers, 42, 7, requirementsFilePath);

        System.out.println("Generating timetable using Prolog...");
        String queryType = "run";
        String outputFilePath = prologIntegrationService.generateTimetable(requirementsFilePath, queryType);

        if (outputFilePath == null || outputFilePath.isEmpty()) {
            System.out.println("Error: output file path is null or empty.");
            throw new RuntimeException("Error generating timetable. No output file returned.");
        }

        System.out.println("Timetable generated successfully. Output file path: " + outputFilePath);

        Timetable timetable = xmlParserService.parseTimetable(outputFilePath);

        if (timetable == null) {
            System.out.println("Error: Timetable parsing failed.");
            throw new RuntimeException("Failed to parse timetable.");
        }

        System.out.println("Timetable parsed successfully. Number of events: " + timetable.getEvents().size());

        Timetable savedTimetable = timetableRepository.save(timetable);

        System.out.println("Timetable saved with ID: " + savedTimetable.getId());

        System.out.println("Saving events...");
        for (Event event : timetable.getEvents()) {
            event.setTimetable(savedTimetable);
        }
        eventRepository.saveAll(timetable.getEvents());

        TimetableDTO timetableDTO = timetableMapper.toTimetableDTO(savedTimetable);

        System.out.println("TimetableDTO created successfully.");

        return timetableDTO;
    }
}
