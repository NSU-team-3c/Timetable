package ru.nsu.timetable.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.RoomDTO;
import ru.nsu.timetable.models.dto.RoomInputDTO;
import ru.nsu.timetable.services.RoomService;
import ru.nsu.timetable.sockets.MessageUtils;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/rooms")
@Tag(name = "Room controller")
public class RoomController {
    private final RoomService roomService;
    private final MessageUtils messageUtils;

    @GetMapping("")
    public List<RoomDTO> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public RoomDTO getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @PostMapping("")
    @Operation(summary = "Create room", security = @SecurityRequirement(name = "bearerAuth"))
    public RoomDTO createRoom(HttpServletRequest request, @RequestBody RoomInputDTO roomInputDTO) {
        RoomDTO roomDTO = roomService.saveRoom(roomInputDTO);
        messageUtils.sendMessage(request, "room", "room " + roomDTO.number() + " created", null);
        return roomDTO;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteRoom(HttpServletRequest request, @PathVariable Long id) {
        String deletedRoomNumber = roomService.deleteRoom(id);
        messageUtils.sendMessage(request, "room", "room " + deletedRoomNumber + " deleted", null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update room", security = @SecurityRequirement(name = "bearerAuth"))
    public RoomDTO updateRoom(HttpServletRequest request, @PathVariable long id, @RequestBody RoomInputDTO roomInputDTO) {
        RoomDTO roomDTO = roomService.updateRoom(id, roomInputDTO);
        messageUtils.sendMessage(request, "room", "room " + roomDTO.number() + " updated", null);
        return roomDTO;
    }
}
