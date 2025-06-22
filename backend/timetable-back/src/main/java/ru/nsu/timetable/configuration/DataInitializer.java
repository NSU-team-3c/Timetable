package ru.nsu.timetable.configuration;

import java.util.Date;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.constants.ERole;
import ru.nsu.timetable.models.entities.Role;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.repositories.RoleRepository;
import ru.nsu.timetable.repositories.UserRepository;

import static ru.nsu.timetable.models.constants.ERole.ROLE_ADMINISTRATOR;
import static ru.nsu.timetable.models.constants.ERole.ROLE_TEACHER;
import static ru.nsu.timetable.models.constants.ERole.ROLE_USER;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.findByName(ROLE_USER).isEmpty()) {
            Role newRole = Role.builder()
                    .name(ERole.ROLE_USER)
                    .build();
            roleRepository.save(newRole);
        }
        if (roleRepository.findByName(ROLE_TEACHER).isEmpty()) {
            Role newRole = Role.builder()
                    .name(ROLE_TEACHER)
                    .build();
            roleRepository.save(newRole);
        }
        if (roleRepository.findByName(ROLE_ADMINISTRATOR).isEmpty()) {
            Role newRole = Role.builder()
                    .name(ROLE_ADMINISTRATOR)
                    .build();
            roleRepository.save(newRole);
        }
        if (userRepository.findByEmail("admin1@example.com").isEmpty()) {
            Role adminRole = roleRepository.findByName(ROLE_ADMINISTRATOR)
                    .orElseThrow(() -> new IllegalStateException("Role ADMINISTRATOR not found"));
            User admin = User.builder()
                    .email("admin1@example.com")
                    .password(new BCryptPasswordEncoder().encode("password"))
                    .dateOfCreation(new Date())
                    .roles(Set.of(adminRole))
                    .build();
            userRepository.save(admin);
        }
        if (userRepository.findByEmail("admin2@example.com").isEmpty()) {
            Role adminRole = roleRepository.findByName(ROLE_ADMINISTRATOR)
                    .orElseThrow(() -> new IllegalStateException("Role ADMINISTRATOR not found"));
            User admin = User.builder()
                    .email("admin2@example.com")
                    .password(new BCryptPasswordEncoder().encode("password"))
                    .dateOfCreation(new Date())
                    .roles(Set.of(adminRole))
                    .build();
            userRepository.save(admin);
        }
    }
}