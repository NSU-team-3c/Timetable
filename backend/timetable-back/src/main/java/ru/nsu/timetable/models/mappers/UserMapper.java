package ru.nsu.timetable.models.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.UserDTO;
import ru.nsu.timetable.models.entities.Role;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.models.constants.ERole;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserMapper {
    public UserDTO toUserDTO(User user) {
        String groupNumber = null;
        if (user.getRoles().stream().map(Role::getName).anyMatch(eRole -> eRole.equals(ERole.ROLE_USER))) {
            if (user.getGroup() != null) {
                groupNumber = user.getGroup().getNumber();
            }
        }
        return new UserDTO(
                user.getEmail(),
                user.getPhone(),
                user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()),
                user.getSurname(),
                user.getName(),
                user.getPatronymic(),
                user.getBirthday(),
                user.getAbout(),
                user.getPhotoUrl(),
                groupNumber
        );
    }
}
