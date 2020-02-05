package org.go.together.dto;

public enum PhotoCategory {
    USER("user photos"),
    EVENT("event photos"),
    PARAMETER("parameter photos"),
    BED_TYPES("bed type photos");

    private String description;

    PhotoCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
