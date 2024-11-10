package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.UserDTO;
import ru.nsu.timetable.models.entities.User;

@Component
public class UserMapper {
    public UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRole().name());
    }

    public User toStudent(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.username());
        user.setRole(User.Role.valueOf(userDTO.role()));
        if (userDTO.id() != null) {
            user.setId(userDTO.id());
        }
        return user;
    }
}
