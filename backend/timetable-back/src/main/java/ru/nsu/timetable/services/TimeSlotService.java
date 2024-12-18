package ru.nsu.timetable.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.TimeSlotDTO;
import ru.nsu.timetable.models.entities.TimeSlot;
import ru.nsu.timetable.models.mappers.TimeSlotMapper;
import ru.nsu.timetable.repositories.TimeSlotRepository;

@RequiredArgsConstructor
@Service
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotMapper timeSlotMapper;

    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotRepository
                .findAll();
    }

    public TimeSlot getTimeSlotById(Long id) {
        return getTimeSlot(id);
    }

    public TimeSlot saveTimeSlot(TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = timeSlotMapper.toTimeSlot(timeSlotDTO);
        return timeSlotRepository.save(timeSlot);
    }

    public void deleteTimeSlot(Long id) {
        if (timeSlotRepository.existsById(id)) {
            timeSlotRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("TimeSlot with id " + id + " not found");
        }
    }

    public TimeSlot updateTimeSlot(Long id, TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = getTimeSlot(id);
        timeSlot.setStartTime(timeSlotDTO.startTime());
        timeSlot.setEndTime(timeSlotDTO.endTime());
        return timeSlotRepository.save(timeSlot);
    }

    private TimeSlot getTimeSlot(Long id) {
        Optional<TimeSlot> timeSlot = timeSlotRepository.findById(id);
        if (timeSlot.isEmpty()) {
            throw new ResourceNotFoundException("TimeSlot with id " + id + " not found");
        } else {
            return timeSlot.get();
        }
    }
}

