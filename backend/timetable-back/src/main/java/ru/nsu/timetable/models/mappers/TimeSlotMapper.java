package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.TimeSlotDTO;
import ru.nsu.timetable.models.entities.TimeSlot;

@Component
public class TimeSlotMapper {
    public TimeSlot toTimeSlot(TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(timeSlotDTO.startTime());
        timeSlot.setEndTime(timeSlotDTO.endTime());
        return timeSlot;
    }
}
