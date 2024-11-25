package ru.nsu.timetable.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.GroupDTO;
import ru.nsu.timetable.services.GroupService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/management")
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/groups")
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/groups/{id}")
    public GroupDTO getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id);
    }

    @PostMapping("/groups")
    public GroupDTO createGroup(@RequestBody GroupDTO groupDTO) {
        return groupService.saveGroup(groupDTO);
    }

    @DeleteMapping("/groups/{id}")
    public void deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
    }
}
