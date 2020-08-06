package org.go.together.notification.dto;

import org.go.together.interfaces.NamedEnum;

public enum EventUserStatus implements NamedEnum {
    WAITING_APPROVE("waiting approve"),
    APPROVED("approved"),
    REJECTED("rejected");

    private final String description;

    EventUserStatus(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
