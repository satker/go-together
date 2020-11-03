package org.go.together.find.logic.interfaces;

import org.go.together.dto.form.FilterDto;
import org.go.together.dto.form.FormDto;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;

import java.util.Map;

public interface BaseCorrectorService {
    Map<FieldDto, FilterDto> getCorrectedFilters(FormDto formDto, Map<String, FieldMapper> mappingFields);
}
