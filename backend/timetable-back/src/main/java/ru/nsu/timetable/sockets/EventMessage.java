package ru.nsu.timetable.sockets;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventMessage {
    private String object;
    private String message;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String subMessage;
}
