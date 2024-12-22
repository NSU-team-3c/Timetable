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
        return Group.builder()
                .number(groupInputDTO.number())
                .course(groupInputDTO.course())
                .capacity(groupInputDTO.capacity())
                .department(groupInputDTO.department())
                .build();
    }
}
