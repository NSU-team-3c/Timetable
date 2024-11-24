package ru.nsu.timetable.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.CourseDTO;
import ru.nsu.timetable.models.entities.Course;
import ru.nsu.timetable.models.mappers.CourseMapper;
import ru.nsu.timetable.repositories.CourseRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public List<CourseDTO> getAllCourses() {
        return courseRepository
                .findAll()
                .stream()
                .map(courseMapper::toCourseDTO)
                .toList();
    }

    public CourseDTO getCourseById(long id) {
        return courseMapper.toCourseDTO(getCourse(id));
    }

    public CourseDTO saveCourse(CourseDTO courseDTO) {
        Course course = courseMapper.toCourse(courseDTO);
        return courseMapper.toCourseDTO(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Course with id " + id + " not found");
        }
    }

    private Course getCourse(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            throw new ResourceNotFoundException("Course with id " + id + " not found");
        } else {
            return course.get();
        }
    }
}
