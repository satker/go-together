package org.go.together.notification.dto;

import org.go.together.interfaces.NamedEnum;

public enum Role implements NamedEnum {
    ROLE_USER("user"),
    ROLE_ADMIN("admin"),
    ROLE_OWNER("owner");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
