package org.go.together.notification.dto;

import org.go.together.interfaces.NamedEnum;

public enum TestNamedEnum implements NamedEnum {
    TEST("test"), ANOTHER_TEST("another test");

    private final String description;

    TestNamedEnum(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
