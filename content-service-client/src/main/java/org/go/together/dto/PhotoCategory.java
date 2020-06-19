package org.go.together.dto;

import org.go.together.interfaces.NamedEnum;

public enum PhotoCategory implements NamedEnum {
    USER("user photos"),
    EVENT("event photos"),
    PARAMETER("parameter photos"),
    BED_TYPES("bed type photos");

    private final String description;

    PhotoCategory(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
