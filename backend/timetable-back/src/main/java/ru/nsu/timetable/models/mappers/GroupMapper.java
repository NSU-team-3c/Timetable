package ru.nsu.timetable.models.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.GroupDTO;
import ru.nsu.timetable.models.entities.Group;
import ru.nsu.timetable.models.entities.Student;
import ru.nsu.timetable.repositories.StudentRepository;

@Component
public class GroupMapper {
    private final StudentRepository studentRepository;

    public GroupMapper(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public GroupDTO toGroupDTO(Group group) {
        return new GroupDTO(group.getId(), group.getName(),
                group.getStudents().stream()
                        .map(Student::getId)
                        .collect(Collectors.toSet()));
    }

    public Group toGroup(GroupDTO groupDTO) {
        Group group = new Group();
        group.setName(groupDTO.name());
        groupDTO.studentIds().forEach(studentId ->
                studentRepository.findById(studentId).ifPresent(group::addStudent)
        );
        if (groupDTO.id() != null) {
            group.setId(groupDTO.id());
        }
        return group;
    }
}
