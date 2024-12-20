package ru.nsu.timetable.services;

import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.constants.ERole;
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.dto.UserDTO;
import ru.nsu.timetable.models.dto.UserInputDTO;
import ru.nsu.timetable.models.entities.Group;
import ru.nsu.timetable.models.entities.Operations;
import ru.nsu.timetable.models.entities.Role;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.models.mappers.UserMapper;
import ru.nsu.timetable.repositories.GroupRepository;
import ru.nsu.timetable.repositories.OperationsRepository;
import ru.nsu.timetable.repositories.RoleRepository;
import ru.nsu.timetable.repositories.UserRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final OperationsRepository operationsRepository;
    private final GroupRepository groupRepository;

    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        } else {
            return user.get();
        }
    }

    public UserDTO getUserInfoByEmail(String email) {
        return userMapper.toUserDTO(getUserByEmail(email));

    }

    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + email + " not found");
        } else {
            return user.get();
        }
    }

    public UserDTO updateUserByEmail(String email, UserInputDTO userInputDTO) {
        User user = getUserByEmail(email);
        user.setPhone(userInputDTO.phone());
        user.setSurname(userInputDTO.surname());
        user.setName(userInputDTO.name());
        user.setPatronymic(userInputDTO.patronymic());
        user.setBirthday(userInputDTO.birthday());
        user.setAbout(userInputDTO.about());
        user.setPhotoUrl(userInputDTO.photoUrl());
        if (user.getRoles().stream().map(Role::getName).anyMatch(eRole -> eRole.equals(ERole.ROLE_USER))) {
            if (userInputDTO.group() != null) {
                Group group = groupRepository.findByNumber(userInputDTO.group()).orElseThrow();
                user.setGroup(group);
            }
        }
        return userMapper.toUserDTO(userRepository.save(user));
    }

    public List<TeacherDTO> getTeachers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles().stream().map(Role::getName).anyMatch(eRole -> eRole.equals(ERole.ROLE_TEACHER)))
                .map(userMapper::toTeacherDTO)
                .toList();
    }

    public boolean existByEmailCheck(String email) {
        return userRepository.existsByEmail(email);
    }

    public String changeUserRoles(String email, Set<String> stringRoles) {
        User userByEmail = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Role> roles = new HashSet<>();
        for (String sRole : stringRoles) {
            Role userRole = roleRepository.findByName(ERole.valueOf(sRole))
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(userRole);
        }
        userByEmail.setRoles(roles);
        userRepository.save(userByEmail);

        saveOperation("Admin", "User with email '" + email + "' changed role");

        return "Role changed for user " + email;
    }

    public User saveNewAdmin(String email, String password, String name, String surname) {
        return saveNewUserWithRole(email, password, ERole.ROLE_ADMINISTRATOR, name, surname);
    }

    public User saveNewTeacher(String email, String password, String name, String surname) {
        return saveNewUserWithRole(email, password, ERole.ROLE_TEACHER, name, surname);
    }

    public User saveNewUser(String email, String password, String name, String surname) {
        return saveNewUserWithRole(email, password, ERole.ROLE_USER, name, surname);
    }

    private User saveNewUserWithRole(String email, String password, ERole role, String name, String surname) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setDateOfCreation(new Date());
        newUser.setName(name);
        newUser.setSurname(surname);

        Role userRole = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        newUser.setRoles(Set.of(userRole));

        newUser.setPassword(encoder.encode(password));
        userRepository.save(newUser);

        saveOperation("Admin", "Created new user with email " + email);

        return newUser;
    }

    private void saveOperation(String userAccount, String description) {
        Operations operations = new Operations();
        operations.setDateOfCreation(new Date());
        operations.setUserAccount(userAccount);
        operations.setDescription(description);
        operationsRepository.save(operations);
    }
}
