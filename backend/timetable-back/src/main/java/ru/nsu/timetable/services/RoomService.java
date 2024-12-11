package ru.nsu.timetable.services;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.RoomDTO;
import ru.nsu.timetable.models.entities.Room;
import ru.nsu.timetable.models.mappers.RoomMapper;
import ru.nsu.timetable.repositories.RoomRepository;

@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public List<RoomDTO> getAllRooms() {
        return roomRepository
                .findAll()
                .stream()
                .map(roomMapper::toRoomDTO)
                .toList();
    }

    public RoomDTO getRoomById(Long id) {
        return roomMapper.toRoomDTO(getRoom(id));
    }

    public RoomDTO saveRoom(RoomDTO roomDTO) {
        Room room = roomMapper.toRoom(roomDTO);
        return roomMapper.toRoomDTO(roomRepository.save(room));
    }

    public void deleteRoom(Long id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Room with id " + id + " not found");
        }
    }

    public RoomDTO updateRoom(Long id, RoomDTO roomDTO) {
        Room room = getRoom(id);
        if (roomDTO.name() != null) {
            room.setName(roomDTO.name());
        }
        if (roomDTO.capacity() > 0) {
            room.setCapacity(roomDTO.capacity());
        }
        if (roomDTO.type() != null) {
            room.setType(Room.RoomType.valueOf(roomDTO.type()));
        }
        return roomMapper.toRoomDTO(roomRepository.save(room));
    }

    private Room getRoom(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isEmpty()) {
            throw new ResourceNotFoundException("Room with id " + id + " not found");
        } else {
            return room.get();
        }
    }
}
