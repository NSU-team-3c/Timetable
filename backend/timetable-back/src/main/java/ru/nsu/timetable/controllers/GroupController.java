package ru.nsu.timetable.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/groups")
@Tag(name = "Group controller")
public class GroupController {
    private final GroupService groupService;

    @GetMapping("")
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public GroupDTO getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id);
    }

    @PostMapping("")
    public GroupDTO createGroup(@RequestBody GroupInputDTO groupInputDTO) {
        return groupService.saveGroup(groupInputDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
    }

    @PutMapping("/{id}")
    public GroupDTO updateGroup(@PathVariable Long id, @RequestBody GroupInputDTO groupInputDTO) {
        return groupService.updateGroup(id, groupInputDTO);
    }
}
