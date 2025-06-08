package ru.nsu.timetable.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.TimeSlotDTO;
import ru.nsu.timetable.models.entities.TimeSlot;
import ru.nsu.timetable.models.entities.User;
import ru.nsu.timetable.models.mappers.TimeSlotMapper;
import ru.nsu.timetable.repositories.TimeSlotRepository;
import ru.nsu.timetable.repositories.UserRepository;

@RequiredArgsConstructor
@Service
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotMapper timeSlotMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public List<TimeSlot> getAllTimeSlotsForTeacher(String email) {
        User user = userService.getUserByEmail(email);
        return user.getAvailableTimeSlots();
    }

    @Transactional
    public List<TimeSlot> saveTimeSlots(String email, List<TimeSlotDTO> timeSlotDTOs) {
        User user = userService.getUserByEmail(email);
        List<TimeSlot> timeSlots = timeSlotDTOs
                .stream()
                .map(timeSlotMapper::toTimeSlot)
                .map(timeSlotRepository::save)
                .toList();
        user.getAvailableTimeSlots().addAll(timeSlots);
        userRepository.save(user);
        return user.getAvailableTimeSlots();
    }

    @Transactional
    public List<TimeSlot> deleteTimeSlots(String email, List<Long> ids) {
        User user = userService.getUserByEmail(email);
        for (Long id : ids) {
            if (timeSlotRepository.existsById(id)) {
                timeSlotRepository.deleteById(id);
            } else {
                throw new ResourceNotFoundException("TimeSlot with id " + id + " not found for teacher " + user.getEmail());
            }
        }
        userRepository.save(user);
        return user.getAvailableTimeSlots();
    }
}

