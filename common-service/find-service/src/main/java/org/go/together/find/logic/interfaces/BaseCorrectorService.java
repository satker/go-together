package org.go.together.find.logic.interfaces;

import org.go.together.compare.FieldMapper;
import org.go.together.dto.form.FilterDto;
import org.go.together.dto.form.FormDto;
import org.go.together.find.dto.FieldDto;

import java.util.Map;
import java.util.UUID;

public interface BaseCorrectorService {
    Map<FieldDto, FilterDto> getCorrectedFilters(UUID requestId, FormDto formDto, Map<String, FieldMapper> mappingFields);
}
