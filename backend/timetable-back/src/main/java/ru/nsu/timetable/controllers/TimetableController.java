package ru.nsu.timetable.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.models.dto.*;
import ru.nsu.timetable.payload.requests.TimetableGenerationRequest;
import ru.nsu.timetable.services.PrologIntegrationService;
import ru.nsu.timetable.services.RequirementsXmlGeneratorService;
import ru.nsu.timetable.services.TimetableService;
import ru.nsu.timetable.services.XmlParserService;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/management")
@Tag(name = "Timetable controller")
public class TimetableController {

    private final TimetableService timetableService;
    private final PrologIntegrationService prologIntegrationService;
    private final XmlParserService xmlParserService;
    private final RequirementsXmlGeneratorService requirementsXmlGeneratorService;

    @GetMapping("/timetables")
    public List<TimetableDTO> getAllTimetables() {
        return timetableService.getAllTimetables();
    }

    @GetMapping("/timetables/{id}")
    public TimetableDTO getTimetableById(@PathVariable Long id) {
        return timetableService.getTimetableById(id);
    }

    @PostMapping("/timetables")
    public TimetableDTO createTimetable(@RequestBody TimetableDTO timetableDTO) {
        return timetableService.saveTimetable(timetableDTO);
    }

    @DeleteMapping("/timetables/{id}")
    public void deleteTimetable(@PathVariable Long id) {
        timetableService.deleteTimetable(id);
    }

    @PostMapping("/timetables/generate")
    public ResponseEntity<TimetableDTO> generateAndSaveTimetable(
            @RequestBody TimetableGenerationRequest request) {
        try {
            List<GroupDTO> groups = request.getGroups();
            List<RoomDTO> rooms = request.getRooms();
            List<TeacherDTO> teachers = request.getTeachers();

            Map<Long, List<RequirementDTO>> requirementsMap = new HashMap<>();
            Map<Long, Set<Integer>> slotsMap = new HashMap<>();
            Map<Long, List<AllocateDTO>> allocateMap = new HashMap<>();

            String requirementsFilePath = "reqs.xml";
            requirementsXmlGeneratorService.generateXml(groups, rooms, teachers, requirementsMap, slotsMap, allocateMap, 42, 7, requirementsFilePath);

            String queryType = "create_timetable";
            String outputFilePath = prologIntegrationService.generateTimetable(requirementsFilePath, queryType);
            TimetableDTO timetableDTO = xmlParserService.parseTimetable(outputFilePath);

            TimetableDTO savedTimetable = timetableService.saveTimetable(timetableDTO);

            return ResponseEntity.ok(savedTimetable);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body(null);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
