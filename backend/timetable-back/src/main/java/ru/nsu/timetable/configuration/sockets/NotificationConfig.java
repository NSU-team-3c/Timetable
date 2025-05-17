package ru.nsu.timetable.configuration.sockets;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.nsu.timetable.models.constants.DestinationPrefixes;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class NotificationConfig {
    private final SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    private void setDefaultDestination() {
        messagingTemplate.setDefaultDestination(DestinationPrefixes.NOTIFICATIONS + "/newLog");
    }
}
