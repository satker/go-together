package org.go.together.find;

import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.ResponseDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.Map;

public interface FindService<E extends IdentifiedEntity> {
    ResponseDto<Object> find(FormDto formDto);

    String getServiceName();

    default Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
