package org.go.together.kafka.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;

import java.util.UUID;

public interface FindProducer<D extends Dto> {
    ResponseDto<Object> find(UUID requestId, FormDto formDto);

    String getConsumerId();
}
