package org.go.together.notification.dto;

import org.go.together.interfaces.NamedEnum;

public enum PhotoCategory implements NamedEnum {
    USER("user photos"),
    EVENT("event photos");

    private final String description;

    PhotoCategory(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
