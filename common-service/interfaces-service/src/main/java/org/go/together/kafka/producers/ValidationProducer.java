package org.go.together.kafka.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;

import java.util.UUID;

public interface ValidationProducer<D extends Dto> {
    ValidationMessageDto validate(UUID requestId, D dto);

    String getConsumerId();
}
