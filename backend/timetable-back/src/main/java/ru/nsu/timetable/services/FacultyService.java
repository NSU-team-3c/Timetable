package ru.nsu.timetable.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.FacultyDTO;
import ru.nsu.timetable.models.entities.Faculty;
import ru.nsu.timetable.models.mappers.FacultyMapper;
import ru.nsu.timetable.repositories.FacultyRepository;

@RequiredArgsConstructor
@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;

    public List<FacultyDTO> getAllFaculties() {
        return facultyRepository
                .findAll()
                .stream()
                .map(facultyMapper::toFacultyDTO)
                .toList();
    }

    public FacultyDTO getFacultyById(long id) {
        return facultyMapper.toFacultyDTO(getFaculty(id));
    }

    public FacultyDTO saveFaculty(FacultyDTO facultyDTO) {
        Faculty faculty = facultyMapper.toFaculty(facultyDTO);
        return facultyMapper.toFacultyDTO(facultyRepository.save(faculty));
    }

    public void deleteFaculty(Long id) {
        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Faculty with id " + id + " not found");
        }
    }

    private Faculty getFaculty(Long id) {
        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isEmpty()) {
            throw new ResourceNotFoundException("Faculty with id " + id + " not found");
        } else {
            return faculty.get();
        }
    }
}