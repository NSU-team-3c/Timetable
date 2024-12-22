package ru.nsu.timetable.models.mappers;


import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.RoomDTO;
import ru.nsu.timetable.models.dto.RoomInputDTO;
import ru.nsu.timetable.models.entities.Room;

@Component
public class RoomMapper {
    public RoomDTO toRoomDTO(Room room) {
        return new RoomDTO(room.getId(), room.getNumber(), room.getCapacity(), room.getType().name());
    }

    public Room toRoom(RoomInputDTO roomInputDTO) {
        return Room.builder()
                .number(roomInputDTO.number())
                .capacity(roomInputDTO.capacity())
                .type(Room.RoomType.valueOf(roomInputDTO.type()))
                .build();
    }
}