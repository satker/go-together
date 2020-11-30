package org.go.together.kafka.enums;

import org.go.together.interfaces.NamedEnum;

public enum ProducerPostfix implements NamedEnum {
    FIND("FindKafkaProducer"),
    DELETE("DeleteKafkaProducer"),
    CREATE("CreateKafkaProducer"),
    UPDATE("UpdateKafkaProducer"),
    VALIDATE("ValidateKafkaProducer"),
    READ("ReadKafkaProducer");

    private final String description;

    ProducerPostfix(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
