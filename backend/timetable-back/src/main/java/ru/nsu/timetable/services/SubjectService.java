package ru.nsu.timetable.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.SubjectDTO;
import ru.nsu.timetable.models.dto.SubjectRequestDTO;
import ru.nsu.timetable.models.entities.Group;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.models.mappers.SubjectMapper;
import ru.nsu.timetable.repositories.GroupRepository;
import ru.nsu.timetable.repositories.SubjectRepository;
import ru.nsu.timetable.repositories.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparingLong(Subject::getId))
                .map(subjectMapper::toSubjectDTO)
                .toList();
    }

    public SubjectDTO getSubjectById(Long id) {
        return subjectMapper.toSubjectDTO(getSubject(id));
    }

    public SubjectDTO saveSubject(SubjectRequestDTO subjectRequestDTO) {
        Subject subject = subjectMapper.toSubject(subjectRequestDTO);
        return subjectMapper.toSubjectDTO(subjectRepository.save(subject));
    }

    public void deleteSubject(Long id) {
        if (subjectRepository.existsById(id)) {
            subjectRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Subject with id " + id + " not found");
        }
    }

    public SubjectDTO updateSubject(@PathVariable long id, @RequestBody SubjectRequestDTO subjectRequestDTO) {
        Subject subject = getSubject(id);
        subject.setName(subjectRequestDTO.name());
        subject.setCode(subjectRequestDTO.code());
        subject.setDescription(subjectRequestDTO.description());
        subject.setDuration(subjectRequestDTO.duration());
        subject.setAudienceType(Subject.AudienceType.valueOf(subjectRequestDTO.audienceType()));

        subject.getGroups().clear();
        List<Group> groups = groupRepository.findAllById(subjectRequestDTO.groupIds());
        subject.getGroups().addAll(groups);

        subject.getTeachers().clear();
        List<User> teachers = userRepository.findAllById(subjectRequestDTO.teacherIds());
        subject.getTeachers().addAll(teachers);

        return subjectMapper.toSubjectDTO(subjectRepository.save(subject));
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
