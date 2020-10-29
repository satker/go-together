package org.go.together.enums;

import org.go.together.interfaces.NamedEnum;

public enum CrudOperation implements NamedEnum {
    CREATE("create"), UPDATE("update"), DELETE("delete");

    private final String description;

    CrudOperation(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
