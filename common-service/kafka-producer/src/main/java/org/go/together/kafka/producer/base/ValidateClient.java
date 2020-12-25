package org.go.together.kafka.producer.base;

import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.kafka.producers.ValidationProducer;
import org.go.together.kafka.producers.crud.ValidateKafkaProducer;

import java.util.UUID;

public abstract class ValidateClient<D extends Dto> extends FindClient<D> implements ValidationProducer<D> {
    private ValidateKafkaProducer<D> validateKafkaProducer;

    public void setValidateKafkaProducers(ValidateKafkaProducer<D> validateKafkaProducer) {
        this.validateKafkaProducer = validateKafkaProducer;
    }

    @Override
    public ValidationMessageDto validate(UUID requestId, D dto) {
        return validateKafkaProducer.validate(requestId, dto);
    }
}
