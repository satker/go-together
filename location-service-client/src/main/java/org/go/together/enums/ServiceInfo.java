package org.go.together.enums;

import org.go.together.interfaces.NamedEnum;

public enum ServiceInfo implements NamedEnum {
    GROUP_LOCATION("groupLocations");

    private final String description;

    ServiceInfo(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}