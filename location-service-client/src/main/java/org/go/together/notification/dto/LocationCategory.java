package org.go.together.notification.dto;

import org.go.together.interfaces.NamedEnum;

public enum LocationCategory implements NamedEnum {
    USER("user photos"),
    EVENT("event photos");

    private final String description;

    LocationCategory(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
