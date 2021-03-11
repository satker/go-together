package org.go.together.kafka.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;

public interface FindProducer<D extends Dto> {
    ResponseDto<Object> find(FormDto formDto);
}
