package ru.nsu.timetable.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
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
    private final TeacherRepository teacherRepository;
    private final RoomRepository roomRepository;
    private final EventRepository eventRepository;

    public List<TimetableDTO> getAllTimetables() {
        return timetableRepository
                .findAll()
                .stream()
                .map(timetableMapper::toTimetableDTO)
                .toList();
    }

    public TimetableDTO getTimetableById(long id) {
        return timetableMapper.toTimetableDTO(getTimetable(id));
    }

    public Timetable saveTimetable(Timetable timetable) {
        return timetableRepository.save(timetable);
    }

    public void deleteTimetable(Long id) {
        if (timetableRepository.existsById(id)) {
            timetableRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Timetable with id " + id + " not found");
        }
    }

    private Timetable getTimetable(Long id) {
        Optional<Timetable> timetable = timetableRepository.findById(id);
        if (timetable.isEmpty()) {
            throw new ResourceNotFoundException("Timetable with id " + id + " not found");
        } else {
            return timetable.get();
        }
    }

    public TimetableDTO generateAndSaveTimetable() throws IOException, InterruptedException, ParserConfigurationException, TransformerException {
        List<Group> groups = groupRepository.findAll();
        List<Room> rooms = roomRepository.findAll();
        List<Teacher> teachers = teacherRepository.findAll();

        System.out.println("Fetched " + groups.size() + " groups, " + rooms.size() + " rooms, " + teachers.size() + " teachers.");

        String requirementsFilePath = "reqs.xml";

        //requirementsXmlGeneratorService.generateXml(groups, rooms, teachers, 42, 7, requirementsFilePath);

        System.out.println("Generating timetable using Prolog...");
        String queryType = "create_timetable";
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
