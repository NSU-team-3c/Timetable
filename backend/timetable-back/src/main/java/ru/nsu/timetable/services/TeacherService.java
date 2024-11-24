package ru.nsu.timetable.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.entities.Teacher;
import ru.nsu.timetable.models.mappers.TeacherMapper;
import ru.nsu.timetable.repositories.TeacherRepository;

@RequiredArgsConstructor
@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository
                .findAll()
                .stream()
                .map(teacherMapper::toTeacherDTO)
                .toList();
    }

    public TeacherDTO getTeacherById(Long id) {
        return teacherMapper.toTeacherDTO(getTeacher(id));
    }

    public TeacherDTO saveTeacher(TeacherDTO teacherDTO) {
        Teacher course = teacherMapper.toTeacher(teacherDTO);
        return teacherMapper.toTeacherDTO(teacherRepository.save(course));
    }

    public void deleteTeacher(Long id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Course with id " + id + " not found");
        }
    }

    private Teacher getTeacher(Long id) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isEmpty()) {
            throw new ResourceNotFoundException("Course with id " + id + " not found");
        } else {
            return teacher.get();
        }
    }
}
