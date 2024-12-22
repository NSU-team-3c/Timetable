package ru.nsu.timetable.models.mappers;

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
        return Subject.builder()
                .name(subjectRequestDTO.name())
                .code(subjectRequestDTO.code())
                .description(subjectRequestDTO.description())
                .duration(subjectRequestDTO.duration())
                .audienceType(Subject.AudienceType.valueOf(subjectRequestDTO.audienceType()))
                .teachers(userRepository.findAllById(subjectRequestDTO.teacherIds()))
                .groups(groupRepository.findAllById(subjectRequestDTO.groupIds()))
                .build();
    }
}
