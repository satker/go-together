package org.go.together.find.correction;

import org.go.together.dto.FieldMapper;
import org.go.together.dto.form.FilterDto;
import org.go.together.find.dto.FieldDto;

import java.util.Map;

public interface CorrectorService {
    Map<FieldDto, FilterDto> getCorrectedFilters(Map<String, FilterDto> filters,
                                                 Map<String, FieldMapper> availableFields);
}
