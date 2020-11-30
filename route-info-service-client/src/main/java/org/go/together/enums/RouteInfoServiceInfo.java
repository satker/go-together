package org.go.together.enums;

import org.go.together.interfaces.NamedEnum;

public enum RouteInfoServiceInfo implements NamedEnum {
    GROUP_ROUTE_INFO("groupRouteInfo");

    private final String description;

    RouteInfoServiceInfo(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
