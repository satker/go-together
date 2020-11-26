package org.go.together.find.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.go.together.kafka.producers.crud.FindKafkaProducer;

@Getter
@Builder
@EqualsAndHashCode(exclude = {"fieldDto"})
public class ClientLocalFieldObject {
    private final FindKafkaProducer client;
    private final FieldDto fieldDto;
    private final String mainIdFeild;
}
