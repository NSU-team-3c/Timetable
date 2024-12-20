package ru.nsu.timetable.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.timetable.models.entities.Timetable;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
}
