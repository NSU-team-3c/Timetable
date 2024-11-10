package ru.nsu.timetable.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.timetable.models.dto.GroupDTO;
import ru.nsu.timetable.models.entities.Group;
import ru.nsu.timetable.models.mappers.GroupMapper;
import ru.nsu.timetable.services.GroupService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @Autowired
    public GroupController(GroupService groupService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @GetMapping
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups()
                .stream()
                .map(groupMapper::toGroupDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id)
                .map(groupMapper::toGroupDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) {
        Group group = groupMapper.toGroup(groupDTO);
        Group savedGroup = groupService.saveGroup(group);
        return ResponseEntity.created(URI.create("/groups/" + savedGroup.getId()))
                .body(groupMapper.toGroupDTO(savedGroup));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        Optional<Group> group = groupService.getGroupById(id);
        if (group.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}
