package ru.nsu.timetable.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.RoomDTO;
import ru.nsu.timetable.models.entities.Room;
import ru.nsu.timetable.models.mappers.RoomMapper;
import ru.nsu.timetable.services.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @Autowired
    public RoomController(RoomService roomService, RoomMapper roomMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    @GetMapping
    public List<RoomDTO> getAllRooms() {
        return roomService.getAllRooms()
                .stream()
                .map(roomMapper::toRoomDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(roomMapper::toRoomDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@RequestBody RoomDTO roomDTO) {
        Room room = roomMapper.toRoom(roomDTO);
        Room savedRoom = roomService.saveRoom(room);
        return ResponseEntity.created(URI.create("/rooms/" + savedRoom.getId()))
                .body(roomMapper.toRoomDTO(savedRoom));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomById(id);
        if (room.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
