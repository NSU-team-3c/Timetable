package ru.nsu.timetable.models.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.CourseDTO;
import ru.nsu.timetable.models.entities.Course;
import ru.nsu.timetable.models.entities.Group;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.repositories.GroupRepository;
import ru.nsu.timetable.repositories.SubjectRepository;

@Component
public class CourseMapper {
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;

    public CourseMapper(GroupRepository groupRepository, SubjectRepository subjectRepository) {
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
    }

    public CourseDTO toCourseDTO(Course course) {
        return new CourseDTO(course.getId(), course.getName(),
                course.getGroups().stream()
                        .map(Group::getId)
                        .collect(Collectors.toSet()),
                course.getSubjects().stream()
                        .map(Subject::getId)
                        .collect(Collectors.toSet()));
    }

    public Course toCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setName(courseDTO.name());
        courseDTO.groupIds().forEach(groupId ->
                groupRepository.findById(groupId).ifPresent(course::addGroup)
        );
        courseDTO.subjectIds().forEach(subjectId ->
                subjectRepository.findById(subjectId).ifPresent(course::addSubject));
        if (courseDTO.id() != null) {
            course.setId(courseDTO.id());
        }
        return course;
    }
}
