package org.go.together.notification.dto;

import org.go.together.interfaces.NamedEnum;

public enum HousingType implements NamedEnum {
    COUCHSERFING("Couchserfing"),
    BOOKING("Booking"),
    LOCAL_HOUSING("Local housing");

    private final String description;

    HousingType(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
