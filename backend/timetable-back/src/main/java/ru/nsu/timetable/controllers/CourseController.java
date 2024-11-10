package ru.nsu.timetable.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.timetable.models.dto.CourseDTO;
import ru.nsu.timetable.models.entities.Course;
import ru.nsu.timetable.models.mappers.CourseMapper;
import ru.nsu.timetable.services.CourseService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseController(CourseService courseService, CourseMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses()
                .stream()
                .map(courseMapper::toCourseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(courseMapper::toCourseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        Course course = courseMapper.toCourse(courseDTO);
        Course savedCourse = courseService.saveCourse(course);
        return ResponseEntity.created(URI.create("/courses/" + savedCourse.getId()))
                .body(courseMapper.toCourseDTO(savedCourse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}