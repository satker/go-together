package org.go.together.find;

import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.FormDto;
import org.go.together.find.dto.ResponseDto;

import java.util.Map;

public interface FindService {
    ResponseDto<Object> find(FormDto formDto);

    String getServiceName();

    Map<String, FieldMapper> getMappingFields();
}
