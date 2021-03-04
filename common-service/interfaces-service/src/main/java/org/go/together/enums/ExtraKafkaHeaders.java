package org.go.together.enums;

import org.go.together.interfaces.NamedEnum;

public enum ExtraKafkaHeaders implements NamedEnum {
    SPAN_ID("spanId"),
    TRACE_ID("traceId");

    private final String description;

    ExtraKafkaHeaders(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
