package org.go.together.base;

import org.go.together.dto.FieldMapper;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.interfaces.Dto;

import java.util.Map;

public interface FindService<D extends Dto> {
    ResponseDto<Object> find(FormDto formDto);

    String getServiceName();

    default Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
