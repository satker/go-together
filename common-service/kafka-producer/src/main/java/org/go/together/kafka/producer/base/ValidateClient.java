package org.go.together.kafka.producer.base;

import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.kafka.producers.ValidationProducer;
import org.go.together.kafka.producers.crud.ValidateKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

import static org.go.together.kafka.producer.enums.ProducerPostfix.VALIDATE;

public abstract class ValidateClient<D extends Dto> extends FindClient<D> implements ValidationProducer<D> {
    private ValidateKafkaProducer<D> validateKafkaProducer;

    @Autowired
    public void setValidateKafkaProducers(Map<String, ValidateKafkaProducer<D>> validateKafkaProducers) {
        this.validateKafkaProducer = validateKafkaProducers.get(getConsumerId() + VALIDATE.getDescription());
    }

    @Override
    public ValidationMessageDto validate(UUID requestId, D dto) {
        return validateKafkaProducer.validate(requestId, dto);
    }
}
