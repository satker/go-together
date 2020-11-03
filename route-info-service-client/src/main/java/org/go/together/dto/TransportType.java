package org.go.together.dto;

import org.go.together.interfaces.NamedEnum;

public enum TransportType implements NamedEnum {
    BUS("trip by bus"),
    AIR("trip by air"),
    RAILWAY("trip by railway");

    private final String description;

    TransportType(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
