package ru.nsu.timetable.sockets;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.nsu.timetable.configuration.security.jwt.JwtUtils;
import ru.nsu.timetable.models.entities.UnplacedSubject;

@Component
@RequiredArgsConstructor
public class MessageUtils {
    private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);
    private static final String DESTINATION_URL = "/notifications/newLog";
    private final JwtUtils jwtUtils;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(HttpServletRequest request, String object, String message, List<UnplacedSubject> unplacedSubjects) {
        String email = Optional.ofNullable(request).map(jwtUtils::getEmailFromHeader).orElse(null);
        try {
            messagingTemplate.convertAndSend(DESTINATION_URL,
                    new EventMessage(
                            object,
                            message,
                            email,
                            LocalDateTime.now(),
                            unplacedSubjects
                    ));
            logger.debug("Сообщение отправлено!");
        } catch (MessagingException e) {
            logger.error("Cannot send message to url " + DESTINATION_URL, e);
        }
    }
}
