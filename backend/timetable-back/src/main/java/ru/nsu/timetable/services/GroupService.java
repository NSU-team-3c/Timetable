package ru.nsu.timetable.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.GroupDTO;
import ru.nsu.timetable.models.entities.Group;
import ru.nsu.timetable.models.mappers.GroupMapper;
import ru.nsu.timetable.repositories.GroupRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public List<GroupDTO> getAllGroups() {
        return groupRepository
                .findAll()
                .stream()
                .map(groupMapper::toGroupDTO)
                .toList();
    }

    public GroupDTO getGroupById(Long id) {
        return groupMapper.toGroupDTO(getGroup(id));
    }

    public GroupDTO saveGroup(GroupDTO groupDTO) {
        Group group = groupMapper.toGroup(groupDTO);
        return groupMapper.toGroupDTO(groupRepository.save(group));
    }

    public void deleteGroup(Long id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Group with id " + id + " not found");
        }
    }

    private Group getGroup(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            throw new ResourceNotFoundException("Group with id " + id + " not found");
        } else {
            return group.get();
        }
    }
}