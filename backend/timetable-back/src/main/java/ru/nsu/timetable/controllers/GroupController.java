package ru.nsu.timetable.controllers;

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
import ru.nsu.timetable.models.dto.GroupDTO;
import ru.nsu.timetable.models.dto.GroupInputDTO;
import ru.nsu.timetable.services.GroupService;
import ru.nsu.timetable.sockets.MessageUtils;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/groups")
@Tag(name = "Group controller")
public class GroupController {
    private final GroupService groupService;
    private final MessageUtils messageUtils;

    @GetMapping("")
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public GroupDTO getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id);
    }

    @PostMapping("")
    @Operation(summary = "Create group", security = @SecurityRequirement(name = "bearerAuth"))
    public GroupDTO createGroup(HttpServletRequest request, @RequestBody GroupInputDTO groupInputDTO) {
        GroupDTO groupDTO = groupService.saveGroup(groupInputDTO);
        messageUtils.sendMessage(request, "group", "group " + groupDTO.number() + " created", null);
        return groupDTO;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete group", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteGroup(HttpServletRequest request, @PathVariable Long id) {
        String deletedGroupNumber = groupService.deleteGroup(id);
        messageUtils.sendMessage(request, "group", "group " + deletedGroupNumber + " deleted", null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update group", security = @SecurityRequirement(name = "bearerAuth"))
    public GroupDTO updateGroup(HttpServletRequest request, @PathVariable Long id, @RequestBody GroupInputDTO groupInputDTO) {
        GroupDTO groupDTO = groupService.updateGroup(id, groupInputDTO);
        messageUtils.sendMessage(request, "group", "group " + groupDTO.number() + " updated", null);
        return groupDTO;
    }
}
