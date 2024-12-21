package ru.nsu.timetable.models.mappers;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.SubjectDTO;
import ru.nsu.timetable.models.dto.SubjectRequestDTO;
import ru.nsu.timetable.models.entities.Group;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.repositories.GroupRepository;
import ru.nsu.timetable.repositories.UserRepository;

@RequiredArgsConstructor
@Component
public class SubjectMapper {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public SubjectDTO toSubjectDTO(Subject subject) {
        return new SubjectDTO(subject.getId(), subject.getName(), subject.getCode(), subject.getDescription(),
                subject.getDuration(), subject.getAudienceType().name(),
                subject.getTeachers().stream()
                        .map(User::getId)
                        .collect(Collectors.toList()),
                subject.getGroups().stream()
                        .map(Group::getId)
                        .collect(Collectors.toList())
                );
    }

    public Subject toSubject(SubjectRequestDTO subjectRequestDTO) {
        Subject subject = new Subject();
        subject.setName(subjectRequestDTO.name());
        subject.setCode(subjectRequestDTO.code());
        subject.setDescription(subjectRequestDTO.description());
        subject.setDuration(subjectRequestDTO.duration());
        subject.setAudienceType(Subject.AudienceType.valueOf(subjectRequestDTO.audienceType()));

        List<User> teachers = userRepository.findAllById(subjectRequestDTO.teacherIds());
        subject.getTeachers().addAll(teachers);

        List<Group> groups = groupRepository.findAllById(subjectRequestDTO.groupIds());
        subject.getGroups().addAll(groups);
        return subject;
    }
}
