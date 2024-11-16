package ru.nsu.timetable.services;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.models.constants.ERole;
import ru.nsu.timetable.models.entities.Operations;
import ru.nsu.timetable.models.entities.Role;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.repositories.OperationsRepository;
import ru.nsu.timetable.repositories.RoleRepository;
import ru.nsu.timetable.repositories.UserRepository;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final OperationsRepository operationsRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder encoder, OperationsRepository operationsRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.operationsRepository = operationsRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existByEmailCheck(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String generateRandomPassword() {
        Random random = new Random();
        int randomPassword = 100000 + random.nextInt(900000);
        return String.valueOf(randomPassword);
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

    public String saveNewAdmin(String email, String fullName, String phone) {
        return saveNewUserWithRoles(email, fullName, phone, Set.of(
                ERole.ROLE_USER.name(),
                ERole.ROLE_ADMINISTRATOR.name()));
    }

    public String saveNewTeacher(String email, String fullName, String phone) {
        return saveNewUserWithRoles(email, fullName, phone, Set.of(
                ERole.ROLE_USER.name(),
                ERole.ROLE_TEACHER.name()));
    }

    public String saveNewUser(String email, String fullName, String phone) {
        return saveNewUserWithRoles(email, fullName, phone, Set.of(
                ERole.ROLE_USER.name()));
    }

    private String saveNewUserWithRoles(String email, String fullName, String phone, Set<String> roles) {
        User newUser = new User();
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

        String userPassword = generateRandomPassword();
        newUser.setPassword(encoder.encode(userPassword));
        userRepository.save(newUser);

        saveOperation("Admin", "Created new user with name '" + fullName + "' and email " + email);
        log.info("User password: {}", userPassword);

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
