package org.go.together.enums;

public enum Direction {
    ASC("ASC"),
    DESC("DESC");

    private final String name;

    Direction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
