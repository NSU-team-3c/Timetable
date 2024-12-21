package ru.nsu.timetable.models.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.dto.UserDTO;
import ru.nsu.timetable.models.dto.UserRegisterDTO;
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

        String roles = user.getRoles().stream()
                .map(this::getRoleName)
                .collect(Collectors.joining(", "));

        return new UserDTO(
                user.getEmail(),
                user.getPhone(),
                roles,
                user.getSurname(),
                user.getName(),
                user.getPatronymic(),
                user.getBirthday(),
                user.getAbout(),
                user.getPhotoUrl(),
                groupNumber
        );
    }

    public UserRegisterDTO toUserRegisterDTO(User user) {
        String roles = user.getRoles().stream()
                .map(this::getRoleName)
                .collect(Collectors.joining(", "));

        return new UserRegisterDTO(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                roles
        );
    }

    public TeacherDTO toTeacherDTO(User user) {
        String groupNumber = null;
        if (user.getRoles().stream().map(Role::getName).anyMatch(eRole -> eRole.equals(ERole.ROLE_USER))) {
            if (user.getGroup() != null) {
                groupNumber = user.getGroup().getNumber();
            }
        }

        String roles = user.getRoles().stream()
                .map(this::getRoleName)
                .collect(Collectors.joining(", "));

        return new TeacherDTO(
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                roles,
                user.getSurname(),
                user.getName(),
                user.getPatronymic(),
                user.getBirthday(),
                user.getAbout(),
                user.getPhotoUrl(),
                groupNumber
        );
    }

    private String getRoleName(Role role) {
        return role.getName().name().replace("ROLE_", "").toLowerCase();
    }
}
