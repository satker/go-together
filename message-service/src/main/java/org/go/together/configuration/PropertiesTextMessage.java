package org.go.together.configuration;

import lombok.Data;

import java.util.Map;

@Data
public class PropertiesTextMessage {
    private String sessionId;
    private String userRecipientId;
    private String userId;
    private String eventId;

    public PropertiesTextMessage(String sessionId, Map propertiesMap) {
        this.sessionId = sessionId;
        this.userId = (String) propertiesMap.get("userId");
        this.eventId = (String) propertiesMap.get("eventId");
        this.userRecipientId = (String) propertiesMap.get("userRecipientId");
    }
}
