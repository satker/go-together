package org.go.together.find.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.go.together.kafka.producers.FindProducer;

@Getter
@Builder
@EqualsAndHashCode(exclude = {"fieldDto"})
public class ClientLocalFieldObject {
    private final FindProducer client;
    private final FieldDto fieldDto;
    private final String mainIdFeild;
}
