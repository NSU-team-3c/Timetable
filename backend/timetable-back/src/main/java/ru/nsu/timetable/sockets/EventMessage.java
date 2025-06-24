package ru.nsu.timetable.sockets;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.nsu.timetable.models.entities.UnplacedSubject;

@Data
@AllArgsConstructor
public class EventMessage {
    private String object;
    private String message;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private List<UnplacedSubject> subMessage;
}
