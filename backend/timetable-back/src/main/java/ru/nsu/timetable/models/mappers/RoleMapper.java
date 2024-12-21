package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.constants.ERole;
import ru.nsu.timetable.models.dto.RoleDTO;
import ru.nsu.timetable.models.entities.Role;

@Component
public class RoleMapper {

    public RoleDTO toRoleDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName().name());
    }

    public Role toRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.id());
        role.setName(ERole.valueOf(roleDTO.name()));
        return role;
    }
}
