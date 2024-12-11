package ru.nsu.timetable.models.mappers;


import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.RoomDTO;
import ru.nsu.timetable.models.entities.Room;

@Component
public class RoomMapper {
    public RoomDTO toRoomDTO(Room room) {
        return new RoomDTO(room.getId(), room.getName(), room.getCapacity(), room.getType().name());
    }

    public Room toRoom(RoomDTO roomDTO) {
        Room room = new Room();
        room.setName(roomDTO.name());
        room.setCapacity(roomDTO.capacity());
        room.setType(Room.RoomType.valueOf(roomDTO.type()));
        if (roomDTO.id() != null) {
            room.setId(roomDTO.id());
        }
        return room;
    }
}