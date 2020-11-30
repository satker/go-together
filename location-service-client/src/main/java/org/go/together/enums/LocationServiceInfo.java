package org.go.together.enums;

import org.go.together.interfaces.NamedEnum;

public enum LocationServiceInfo implements NamedEnum {
    GROUP_LOCATION("groupLocations");

    private final String description;

    LocationServiceInfo(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}