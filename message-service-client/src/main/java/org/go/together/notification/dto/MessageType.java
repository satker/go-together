package org.go.together.notification.dto;

import org.go.together.interfaces.NamedEnum;

public enum MessageType implements NamedEnum {
    TO_USER("user"),
    TO_EVENT("event"),
    REVIEW("review");

    private final String description;

    MessageType(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
