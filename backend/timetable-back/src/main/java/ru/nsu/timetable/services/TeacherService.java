package ru.nsu.timetable.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.TeacherDTO;
import ru.nsu.timetable.models.dto.TeacherRequestDTO;
import ru.nsu.timetable.models.dto.TeacherSubjectDTO;
import ru.nsu.timetable.models.dto.TeacherTimeslotDTO;
import ru.nsu.timetable.models.entities.Subject;
import ru.nsu.timetable.models.entities.Teacher;
import ru.nsu.timetable.models.entities.TimeSlot;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.models.mappers.TeacherMapper;
import ru.nsu.timetable.repositories.SubjectRepository;
import ru.nsu.timetable.repositories.TeacherRepository;
import ru.nsu.timetable.repositories.TimeSlotRepository;
import ru.nsu.timetable.repositories.UserRepository;

@RequiredArgsConstructor
@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final SubjectRepository subjectRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final UserRepository userRepository;

    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository
                .findAll()
                .stream()
                .map(teacherMapper::toTeacherDTO)
                .toList();
    }

    public TeacherDTO getTeacherByUserId(Long userId) {
        Optional<Teacher> teacher = teacherRepository.findByUserData_id(userId);
        if (teacher.isEmpty()) {
            throw new ResourceNotFoundException("Teacher with user id " + userId + " not found");
        }
        return teacherMapper.toTeacherDTO(teacher.get());
    }

    public TeacherDTO getTeacherById(Long id) {
        return teacherMapper.toTeacherDTO(getTeacher(id));
    }

    public TeacherDTO saveTeacher(TeacherRequestDTO teacherRequestDTO) {
        Teacher teacher = teacherMapper.toTeacher(teacherRequestDTO);
        return teacherMapper.toTeacherDTO(teacherRepository.save(teacher));
    }

    public void deleteTeacher(Long id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Course with id " + id + " not found");
        }
    }

    public TeacherDTO updateTeacher(Long id, TeacherRequestDTO teacherRequestDTO) {
        Teacher teacher = getTeacher(id);
        if (teacherRequestDTO.organization() != null) {
            teacher.setOrganisation(teacherRequestDTO.organization());
        }
        if (teacherRequestDTO.education() != null) {
            teacher.setOrganisation(teacherRequestDTO.education());
        }
        if (teacherRequestDTO.specialization() != null) {
            teacher.setOrganisation(teacherRequestDTO.specialization());
        }
        if (teacherRequestDTO.userId() > 0) {
            Optional<User> user = userRepository.findById(teacherRequestDTO.userId());
            if (user.isEmpty()) {
                throw new ResourceNotFoundException("User with id " + id + " not found");
            } else {
                teacher.setUserData(user.get());
            }
        }
        return teacherMapper.toTeacherDTO(teacherRepository.save(teacher));
    }

    public TeacherDTO assignSubjectsToTeacher(TeacherSubjectDTO dto) {
        Teacher teacher = getTeacher(dto.teacherId());
        List<Subject> subjects = subjectRepository.findAllById(dto.subjectIds());
        teacher.getSubjects().addAll(subjects);
        return teacherMapper.toTeacherDTO(teacherRepository.save(teacher));
    }

    public TeacherDTO updateSubjectsForTeacher(TeacherSubjectDTO dto) {
        Teacher teacher = getTeacher(dto.teacherId());
        teacher.getSubjects().clear();
        List<Subject> subjects = subjectRepository.findAllById(dto.subjectIds());
        teacher.getSubjects().addAll(subjects);
        return teacherMapper.toTeacherDTO(teacherRepository.save(teacher));
    }

    public TeacherDTO removeSubjectsFromTeacher(TeacherSubjectDTO dto) {
        Teacher teacher = getTeacher(dto.teacherId());
        List<Subject> subjects = subjectRepository.findAllById(dto.subjectIds());
        teacher.getSubjects().removeAll(subjects);
        return teacherMapper.toTeacherDTO(teacherRepository.save(teacher));
    }

    public TeacherDTO assignTimeSlotsToTeacher(TeacherTimeslotDTO dto) {
        Teacher teacher = getTeacher(dto.teacherId());
        List<TimeSlot> timeSlots = timeSlotRepository.findAllById(dto.timeSlotIds());
        teacher.getAvailableTimeSlots().addAll(timeSlots);
        return teacherMapper.toTeacherDTO(teacherRepository.save(teacher));
    }

    public TeacherDTO updateTimeSlotsForTeacher(TeacherTimeslotDTO dto) {
        Teacher teacher = getTeacher(dto.teacherId());
        teacher.getAvailableTimeSlots().clear();
        List<TimeSlot> timeSlots = timeSlotRepository.findAllById(dto.timeSlotIds());
        teacher.getAvailableTimeSlots().addAll(timeSlots);
        return teacherMapper.toTeacherDTO(teacherRepository.save(teacher));
    }

    public TeacherDTO removeTimeSlotsFromTeacher(TeacherTimeslotDTO dto) {
        Teacher teacher = getTeacher(dto.teacherId());
        List<TimeSlot> timeSlots = timeSlotRepository.findAllById(dto.timeSlotIds());
        teacher.getAvailableTimeSlots().removeAll(timeSlots);
        return teacherMapper.toTeacherDTO(teacherRepository.save(teacher));
    }

    private Teacher getTeacher(Long id) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isEmpty()) {
            throw new ResourceNotFoundException("Teacher with id " + id + " not found");
        } else {
            return teacher.get();
        }
    }
}
