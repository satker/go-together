package org.go.together.dto;

import org.go.together.interfaces.NamedEnum;

public enum LocationCategory implements NamedEnum {
    USER("user locations"),
    EVENT("event locations");

    private final String description;

    LocationCategory(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
