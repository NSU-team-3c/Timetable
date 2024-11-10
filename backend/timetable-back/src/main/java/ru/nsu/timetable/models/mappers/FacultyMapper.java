package ru.nsu.timetable.models.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.FacultyDTO;
import ru.nsu.timetable.models.entities.Course;
import ru.nsu.timetable.models.entities.Faculty;
import ru.nsu.timetable.repositories.CourseRepository;

@Component
public class FacultyMapper {
    private final CourseRepository courseRepository;

    public FacultyMapper(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public FacultyDTO toFacultyDTO(Faculty faculty) {
        return new FacultyDTO(faculty.getId(), faculty.getName(),
                faculty.getCourses().stream()
                        .map(Course::getId)
                        .collect(Collectors.toSet()));
    }

    public Faculty toFaculty(FacultyDTO facultyDTO) {
        Faculty faculty = new Faculty();
        faculty.setName(facultyDTO.name());
        facultyDTO.courseIds().forEach(groupId ->
                courseRepository.findById(groupId).ifPresent(faculty::addCourse)
        );
        if (facultyDTO.id() != null) {
            faculty.setId(facultyDTO.id());
        }
        return faculty;
    }
}
