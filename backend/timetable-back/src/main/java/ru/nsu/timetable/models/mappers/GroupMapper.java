package ru.nsu.timetable.models.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.GroupDTO;
import ru.nsu.timetable.models.entities.Group;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.repositories.SubjectRepository;

@Component
public class GroupMapper {
    private final SubjectRepository subjectRepository;

    public GroupMapper(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public GroupDTO toGroupDTO(Group group) {
        return new GroupDTO(group.getId(), group.getName(), group.getStudents(),
                group.getSubjects().stream()
                        .map(Subject::getId)
                        .collect(Collectors.toList()));
    }

    public Group toGroup(GroupDTO groupDTO) {
        Group group = new Group();
        group.setName(groupDTO.name());
        group.setStudents(groupDTO.students());
        group.setSubjects(subjectRepository.findAllById(groupDTO.subjectIds()));
        if (groupDTO.id() != null) {
            group.setId(groupDTO.id());
        }
        return group;
    }
}
