package org.go.together.enums;

import org.go.together.interfaces.NamedEnum;

public enum TopicKafkaPostfix implements NamedEnum {
    CREATE("Create"),
    UPDATE("Update"),
    DELETE("Delete"),
    READ("Read"),
    VALIDATE("Validate"),
    FIND("Find");

    private final String description;

    TopicKafkaPostfix(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
