package ru.nsu.timetable.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.RoomDTO;
import ru.nsu.timetable.services.RoomService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/management")
@Tag(name = "Room controller")
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/rooms")
    public List<RoomDTO> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/rooms/{id}")
    public RoomDTO getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @PostMapping("/rooms")
    public RoomDTO createRoom(@RequestBody RoomDTO roomDTO) {
        return roomService.saveRoom(roomDTO);
    }

    @DeleteMapping("/rooms/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
}
