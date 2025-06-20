package ru.nsu.timetable.services;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.timetable.exceptions.InvalidDataException;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.RoomDTO;
import ru.nsu.timetable.models.dto.RoomInputDTO;
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
                .sorted(Comparator.comparingLong(Room::getId))
                .map(roomMapper::toRoomDTO)
                .toList();
    }

    public RoomDTO getRoomById(Long id) {
        return roomMapper.toRoomDTO(getRoom(id));
    }

    @Transactional
    public RoomDTO saveRoom(RoomInputDTO roomInputDTO) {
        if (roomRepository.existsByNumber(roomInputDTO.number())) {
            throw new InvalidDataException("Room with number " + roomInputDTO.number() + " already exists");
        }
        Room room = roomMapper.toRoom(roomInputDTO);
        return roomMapper.toRoomDTO(roomRepository.save(room));
    }

    @Transactional
    public String deleteRoom(Long id) {
        if (roomRepository.existsById(id)) {
            String deletedRoomNumber = roomRepository.getReferenceById(id).getNumber();
            roomRepository.deleteById(id);
            return deletedRoomNumber;
        } else {
            throw new ResourceNotFoundException("Room with id " + id + " not found");
        }
    }

    @Transactional
    public RoomDTO updateRoom(Long id, RoomInputDTO roomInputDTO) {
        Room room = getRoom(id);
        room.setNumber(roomInputDTO.number());
        room.setCapacity(roomInputDTO.capacity());
        room.setType(Room.RoomType.valueOf(roomInputDTO.type()));
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
