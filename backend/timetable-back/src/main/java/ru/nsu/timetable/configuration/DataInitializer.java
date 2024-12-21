package ru.nsu.timetable.configuration;

import java.util.Date;
import java.util.List;

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
            Role newRole = new Role();
            newRole.setName(ERole.ROLE_USER);
            roleRepository.save(newRole);
        }
        if (roleRepository.findByName(ROLE_TEACHER).isEmpty()) {
            Role newRole = new Role();
            newRole.setName(ROLE_TEACHER);
            roleRepository.save(newRole);
        }
        if (roleRepository.findByName(ROLE_ADMINISTRATOR).isEmpty()) {
            Role newRole = new Role();
            newRole.setName(ROLE_ADMINISTRATOR);
            roleRepository.save(newRole);
        }
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(new BCryptPasswordEncoder().encode("password")); // Хеширование пароля
            admin.setDateOfCreation(new Date());
            Role adminRole = roleRepository.findByName(ROLE_ADMINISTRATOR)
                    .orElseThrow(() -> new IllegalStateException("Role ADMINISTRATOR not found"));
            Role userRole = roleRepository.findByName(ROLE_USER)
                    .orElseThrow(() -> new IllegalStateException("Role USER not found"));
            admin.getRoles().addAll(List.of(adminRole, userRole));
            userRepository.save(admin);
        }
    }
}