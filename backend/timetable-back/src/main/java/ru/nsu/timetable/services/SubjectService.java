package ru.nsu.timetable.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.SubjectDTO;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.models.mappers.SubjectMapper;
import ru.nsu.timetable.repositories.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository
                .findAll()
                .stream()
                .map(subjectMapper::toSubjectDTO)
                .toList();
    }

    public SubjectDTO getSubjectById(Long id) {
        return subjectMapper.toSubjectDTO(getSubject(id));
    }

    public SubjectDTO saveSubject(SubjectDTO subjectDTO) {
        Subject subject = subjectMapper.toSubject(subjectDTO);
        return subjectMapper.toSubjectDTO(subjectRepository.save(subject));
    }

    public void deleteSubject(Long id) {
        if (subjectRepository.existsById(id)) {
            subjectRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Subject with id " + id + " not found");
        }
    }

    private Subject getSubject(Long id) {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isEmpty()) {
            throw new ResourceNotFoundException("Subject with id " + id + " not found");
        } else {
            return subject.get();
        }
    }
}
