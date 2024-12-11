package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.TimeSlotDTO;
import ru.nsu.timetable.models.entities.TimeSlot;

@Component
public class TimeSlotMapper {
    public TimeSlotDTO toTimeSlotDTO(TimeSlot timeSlot) {
        return new TimeSlotDTO(timeSlot.getId(), timeSlot.getStartTime(), timeSlot.getEndTime());
    }

    public TimeSlot toTimeSlot(TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(timeSlotDTO.startTime());
        timeSlot.setEndTime(timeSlotDTO.endTime());
        if (timeSlotDTO.id() != null) {
            timeSlot.setId(timeSlotDTO.id());
        }
        return timeSlot;
    }
}
