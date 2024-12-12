package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.UserDTO;
import ru.nsu.timetable.models.entities.Role;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.models.constants.ERole;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getFullName(),
                user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet())
        );
    }

    public User toUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.id());
        user.setUsername(userDTO.username());
        user.setFullName(userDTO.fullName());
        user.setEmail(userDTO.email());
        user.setPhone(userDTO.phone());
        if (userDTO.roles() != null) {
            userDTO.roles().forEach(role -> {
                Role roleEntity = new Role(ERole.valueOf(role));
                user.getRoles().add(roleEntity);
            });
        }
        return user;
    }
}
