package ru.nsu.timetable.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.SubjectDTO;
import ru.nsu.timetable.models.dto.SubjectGroupDTO;
import ru.nsu.timetable.models.dto.SubjectRequestDTO;
import ru.nsu.timetable.models.entities.Group;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.models.mappers.SubjectMapper;
import ru.nsu.timetable.repositories.GroupRepository;
import ru.nsu.timetable.repositories.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final GroupRepository groupRepository;

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
        if (subjectRequestDTO.name() != null) {
            subject.setName(subjectRequestDTO.name());
        }
        if (subjectRequestDTO.code() != null) {
            subject.setCode(subjectRequestDTO.code());
        }
        if (subjectRequestDTO.description() != null) {
            subject.setDescription(subjectRequestDTO.description());
        }
        if (subjectRequestDTO.duration() > 0) {
            subject.setDuration(subjectRequestDTO.duration());
        }
        if (subjectRequestDTO.audienceType() != null) {
            subject.setAudienceType(Subject.AudienceType.valueOf(subjectRequestDTO.audienceType()));
        }
        return subjectMapper.toSubjectDTO(subjectRepository.save(subject));
    }

    public SubjectDTO assignGroupsToSubject(SubjectGroupDTO dto) {
        Subject subject = getSubject(dto.subjectId());
        List<Group> groups = groupRepository.findAllById(dto.groupIds());
        subject.getGroups().addAll(groups);
        return subjectMapper.toSubjectDTO(subjectRepository.save(subject));
    }

    public SubjectDTO updateGroupsForSubject(SubjectGroupDTO dto) {
        Subject subject = getSubject(dto.subjectId());
        subject.getGroups().clear();
        List<Group> groups = groupRepository.findAllById(dto.groupIds());
        subject.getGroups().addAll(groups);
        return subjectMapper.toSubjectDTO(subjectRepository.save(subject));
    }

    public SubjectDTO removeGroupsFromSubject(SubjectGroupDTO dto) {
        Subject subject = getSubject(dto.subjectId());
        List<Group> groups = groupRepository.findAllById(dto.groupIds());
        subject.getGroups().removeAll(groups);
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
