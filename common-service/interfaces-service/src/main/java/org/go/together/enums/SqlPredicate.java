package org.go.together.enums;

import org.go.together.interfaces.NamedEnum;

public enum SqlPredicate implements NamedEnum {
    OR(" or "),
    AND(" and ");

    private final String description;

    SqlPredicate(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
