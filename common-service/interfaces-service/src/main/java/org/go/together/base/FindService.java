package org.go.together.base;

import org.go.together.compare.FieldMapper;
import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;

import java.util.Map;

public interface FindService<D extends Dto> {
    ResponseDto<Object> find(FormDto formDto);

    String getServiceName();

    default Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
