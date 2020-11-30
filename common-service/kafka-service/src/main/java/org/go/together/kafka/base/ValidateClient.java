package org.go.together.kafka.base;

import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.kafka.producers.ValidationProducer;
import org.go.together.kafka.producers.crud.ValidateKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

public abstract class ValidateClient<D extends Dto> extends FindClient<D> implements ValidationProducer<D> {
    private ValidateKafkaProducer<D> validateKafkaProducer;

    @Autowired
    public void setValidateKafkaProducers(Map<String, ValidateKafkaProducer<D>> validateKafkaProducers) {
        this.validateKafkaProducer = validateKafkaProducers.get(getConsumerId());
    }

    @Override
    public ValidationMessageDto validate(UUID requestId, D dto) {
        return validateKafkaProducer.validate(requestId, dto);
    }
}
