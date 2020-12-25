package org.go.together.kafka.producer.base;

import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.kafka.producers.FindProducer;
import org.go.together.kafka.producers.crud.FindKafkaProducer;

import java.util.UUID;

public abstract class FindClient<D extends Dto> implements FindProducer<D> {
    private FindKafkaProducer<D> findKafkaProducer;

    public void setFindKafkaProducer(FindKafkaProducer<D> findKafkaProducer) {
        this.findKafkaProducer = findKafkaProducer;
    }

    @Override
    public ResponseDto<Object> find(UUID requestId, FormDto formDto) {
        return findKafkaProducer.find(requestId, formDto);
    }
}
