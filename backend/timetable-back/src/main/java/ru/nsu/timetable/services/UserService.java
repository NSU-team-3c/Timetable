package ru.nsu.timetable.services;

import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.constants.ERole;
import ru.nsu.timetable.models.dto.UserDTO;
import ru.nsu.timetable.models.entities.Operations;
import ru.nsu.timetable.models.entities.Role;
import ru.nsu.timetable.models.entities.Room;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.models.mappers.UserMapper;
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

    public List<UserDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toUserDTO)
                .toList();
    }

    public UserDTO getUserById(Long id) {
        return userMapper.toUserDTO(getUser(id));
    }

    public UserDTO saveUser(UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        return userMapper.toUserDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
    }

    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        } else {
            return user.get();
        }
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

    public String saveNewAdmin(String email, String username, String fullName, String phone, String password) {
        return saveNewUserWithRoles(email, username, fullName, phone, password, Set.of(
                ERole.ROLE_USER.name(),
                ERole.ROLE_ADMINISTRATOR.name()));
    }

    public String saveNewTeacher(String email, String username, String fullName, String phone, String password) {
        return saveNewUserWithRoles(email, username, fullName, phone, password, Set.of(
                ERole.ROLE_USER.name(),
                ERole.ROLE_TEACHER.name()));
    }

    public String saveNewUser(String email, String username, String fullName, String phone, String password) {
        return saveNewUserWithRoles(email, username, fullName, phone, password, Set.of(
                ERole.ROLE_USER.name()));
    }

    private String saveNewUserWithRoles(String email, String username, String fullName, String phone, String password, Set<String> roles) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setDateOfCreation(new Date());
        newUser.setPhone(phone);

        Set<Role> userRoles = new HashSet<>();
        for (String role : roles) {
            Role userRole = roleRepository.findByName(ERole.valueOf(role))
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            userRoles.add(userRole);
        }
        newUser.setRoles(userRoles);

        newUser.setPassword(encoder.encode(password));
        userRepository.save(newUser);

        saveOperation("Admin", "Created new user with name '" + fullName + "' and email " + email);

        return "User " + fullName + " created";
    }

    private void saveOperation(String userAccount, String description) {
        Operations operations = new Operations();
        operations.setDateOfCreation(new Date());
        operations.setUserAccount(userAccount);
        operations.setDescription(description);
        operationsRepository.save(operations);
    }
}
