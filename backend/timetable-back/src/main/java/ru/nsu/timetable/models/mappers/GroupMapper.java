package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.GroupDTO;
import ru.nsu.timetable.models.dto.GroupInputDTO;
import ru.nsu.timetable.models.entities.Group;

@Component
public class GroupMapper {
    public GroupDTO toGroupDTO(Group group) {
        return new GroupDTO(group.getId(), group.getNumber(), group.getCourse(), group.getDepartment(), group.getCapacity());
    }

    public Group toGroup(GroupInputDTO groupInputDTO) {
        Group group = new Group();
        group.setNumber(groupInputDTO.number());
        group.setCourse(groupInputDTO.course());
        group.setDepartment(groupInputDTO.department());
        group.setCapacity(groupInputDTO.capacity());
        return group;
    }
}
