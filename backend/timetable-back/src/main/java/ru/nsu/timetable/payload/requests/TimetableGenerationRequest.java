package ru.nsu.timetable.payload.requests;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.timetable.models.dto.GroupDTO;
import ru.nsu.timetable.models.dto.RoomDTO;
import ru.nsu.timetable.models.dto.TeacherDTO;

import java.util.List;

@Setter
@Getter
public class TimetableGenerationRequest {
    private List<GroupDTO> groups;
    private List<RoomDTO> rooms;
    private List<TeacherDTO> teachers;
}
