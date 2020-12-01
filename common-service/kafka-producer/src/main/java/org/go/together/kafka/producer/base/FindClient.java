package org.go.together.kafka.producer.base;

import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.kafka.producers.FindProducer;
import org.go.together.kafka.producers.crud.FindKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

import static org.go.together.kafka.producer.enums.ProducerPostfix.FIND;

public abstract class FindClient<D extends Dto> implements FindProducer<D> {
    private FindKafkaProducer<D> findKafkaProducer;

    @Autowired
    public void setFindKafkaProducer(Map<String, FindKafkaProducer<D>> findKafkaProducers) {
        this.findKafkaProducer = findKafkaProducers.get(getConsumerId() + FIND.getDescription());
    }

    @Override
    public ResponseDto<Object> find(UUID requestId, FormDto formDto) {
        return findKafkaProducer.find(requestId, formDto);
    }
}
