package org.go.together;

import org.go.together.dto.FieldMapper;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;

import java.util.Map;

public interface FindService {
    ResponseDto<Object> find(FormDto formDto);

    String getServiceName();

    Map<String, FieldMapper> getMappingFields();
}
