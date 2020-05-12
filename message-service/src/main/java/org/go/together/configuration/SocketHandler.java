package org.go.together.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class SocketHandler extends TextWebSocketHandler {
    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    Set<PropertiesTextMessage> messages = new HashSet<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {
        PropertiesTextMessage propertiesTextMessage =
                new PropertiesTextMessage(session.getId(), new ObjectMapper().readValue(message.getPayload(), Map.class));
        messages.add(propertiesTextMessage);
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
                if (checkSessionToSent(webSocketSession.getId(), propertiesTextMessage)) {
                    System.out.println("handleTextMessage " + messages.size() + " " + propertiesTextMessage);
                    webSocketSession.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    private boolean checkSessionToSent(String sessionId, PropertiesTextMessage currentPropertiesTextMessage) {
        Set<PropertiesTextMessage> passedSessions = messages.stream()
                .filter(propertiesTextMessage -> !propertiesTextMessage.equals(currentPropertiesTextMessage))
                .filter(propertiesTextMessage -> propertiesTextMessage.getSessionId().equals(sessionId)).collect(Collectors.toSet());
        return passedSessions.size() == 0 || passedSessions.stream()
                .filter(propertiesTextMessage -> propertiesTextMessage.getEventId().equals(currentPropertiesTextMessage.getEventId()))
                .anyMatch(propertiesTextMessage -> propertiesTextMessage.getUserId().equals(currentPropertiesTextMessage.getUserRecipientId()));
    }
}
