package org.go.together.base;

import org.go.together.compare.FieldMapper;
import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;

import java.util.Map;
import java.util.UUID;

public interface FindService<D extends Dto> {
    default ResponseDto<Object> find(FormDto formDto) {
        return find(UUID.randomUUID(), formDto);
    }

    ResponseDto<Object> find(UUID requestId, FormDto formDto);

    String getServiceName();

    default Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
