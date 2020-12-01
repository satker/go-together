package org.go.together.enums;

import org.go.together.interfaces.NamedEnum;

public enum ContentServiceInfo implements NamedEnum {
    GROUP_PHOTO_NAME("groupPhotos");

    private final String description;

    ContentServiceInfo(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}