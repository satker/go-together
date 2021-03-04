package org.go.together.kafka.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;

public interface ValidationProducer<D extends Dto> {
    ValidationMessageDto validate(D dto);
}
