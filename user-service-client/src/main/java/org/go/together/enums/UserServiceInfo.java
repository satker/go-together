package org.go.together.enums;

import org.go.together.interfaces.NamedEnum;

public enum UserServiceInfo implements NamedEnum {
    USERS("users"),
    EVENT_LIKES("eventLikes"),
    AUTH_USER("authUser");

    private final String description;

    UserServiceInfo(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}