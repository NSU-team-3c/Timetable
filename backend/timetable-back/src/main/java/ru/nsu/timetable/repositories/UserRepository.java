package ru.nsu.timetable.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.models.constants.ERole;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    boolean existsByFullName(String fullName);

    boolean existsByEmail(String email);

    @Query("SELECT u.fullName FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<String> findAllUsersByRole(@Param("roleName") ERole roleName);
}
