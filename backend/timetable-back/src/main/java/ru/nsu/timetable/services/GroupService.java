package ru.nsu.timetable.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.timetable.exceptions.InvalidDataException;
import ru.nsu.timetable.exceptions.ResourceNotFoundException;
import ru.nsu.timetable.models.dto.GroupDTO;
import ru.nsu.timetable.models.dto.GroupInputDTO;
import ru.nsu.timetable.models.entities.Group;
import ru.nsu.timetable.models.mappers.GroupMapper;
import ru.nsu.timetable.repositories.GroupRepository;

import java.util.Comparator;
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
                .sorted(Comparator.comparingLong(Group::getId))
                .map(groupMapper::toGroupDTO)
                .toList();
    }

    public GroupDTO getGroupById(Long id) {
        return groupMapper.toGroupDTO(getGroup(id));
    }

    @Transactional
    public GroupDTO saveGroup(GroupInputDTO groupInputDTO) {
        if (groupRepository.existsByNumber(groupInputDTO.number())) {
            throw new InvalidDataException("Group with number " + groupInputDTO.number() + " already exists");
        }
        Group group = groupMapper.toGroup(groupInputDTO);
        return groupMapper.toGroupDTO(groupRepository.save(group));
    }

    @Transactional
    public String deleteGroup(Long id) {
        if (groupRepository.existsById(id)) {
            String deletedGroupNumber = groupRepository.getReferenceById(id).getNumber();
            groupRepository.deleteById(id);
            return deletedGroupNumber;
        } else {
            throw new ResourceNotFoundException("Group with id " + id + " not found");
        }
    }

    @Transactional
    public GroupDTO updateGroup(Long id, GroupInputDTO groupInputDTO) {
        Group group = getGroup(id);
        group.setNumber(groupInputDTO.number());
        group.setCourse(groupInputDTO.course());
        group.setDepartment(groupInputDTO.department());
        group.setCapacity(groupInputDTO.capacity());
        return groupMapper.toGroupDTO(groupRepository.save(group));
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