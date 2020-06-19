package org.go.together.dto;

import org.go.together.interfaces.NamedEnum;

public enum CashCategory implements NamedEnum {
    FIFTY_FIFTY("50 / 50"),
    FOR_FREE("Free"),
    PAYED_YOURSELF("Yourself");

    private final String description;

    CashCategory(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
