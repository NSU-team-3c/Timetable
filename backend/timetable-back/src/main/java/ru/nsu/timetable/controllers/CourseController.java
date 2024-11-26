package ru.nsu.timetable.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.CourseDTO;
import ru.nsu.timetable.services.CourseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/management")
@Tag(name = "Course controller")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses")
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/courses/{id}")
    public CourseDTO getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping("courses")
    public CourseDTO createCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.saveCourse(courseDTO);
    }

    @DeleteMapping("courses/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}