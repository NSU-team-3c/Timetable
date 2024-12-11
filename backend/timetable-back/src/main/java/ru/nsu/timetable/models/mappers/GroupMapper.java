package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.GroupDTO;
import ru.nsu.timetable.models.entities.Group;

@Component
public class GroupMapper {

    public GroupDTO toGroupDTO(Group group) {
        return new GroupDTO(group.getId(), group.getName(), group.getStudents());
    }

    public Group toGroup(GroupDTO groupDTO) {
        Group group = new Group();
        group.setName(groupDTO.name());
        group.setStudents(groupDTO.students());
        if (groupDTO.id() != null) {
            group.setId(groupDTO.id());
        }
        return group;
    }
}
