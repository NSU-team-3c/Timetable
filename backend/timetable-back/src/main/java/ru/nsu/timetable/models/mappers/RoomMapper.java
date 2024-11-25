package ru.nsu.timetable.models.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.RoomDTO;
import ru.nsu.timetable.models.entities.Room;
import ru.nsu.timetable.models.entities.TimeSlot;
import ru.nsu.timetable.repositories.TimeSlotRepository;

@Component
public class RoomMapper {
    private final TimeSlotRepository timeSlotRepository;

    public RoomMapper(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public RoomDTO toRoomDTO(Room room) {
        return new RoomDTO(room.getId(), room.getName(), room.getCapacity(), room.getType().name(),
                room.getOccupiedTimeSlots().stream()
                        .map(TimeSlot::getId)
                        .collect(Collectors.toSet()));
    }

    public Room toRoom(RoomDTO roomDTO) {
        Room room = new Room();
        room.setName(roomDTO.name());
        room.setCapacity(roomDTO.capacity());
        room.setType(Room.RoomType.valueOf(roomDTO.type()));
        roomDTO.occupiedTimeSlotsIds().forEach(timeSlotId ->
                timeSlotRepository.findById(timeSlotId).ifPresent(room::addOccupiedTimeSlot)
        );
        if (roomDTO.id() != null) {
            room.setId(roomDTO.id());
        }
        return room;
    }
}