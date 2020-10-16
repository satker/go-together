package org.go.together.find.correction;

import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;

import java.util.Map;

public interface CorrectorService {
    Map<FieldDto, FilterDto> getCorrectedFilters(Map<String, FilterDto> filters,
                                                 Map<String, FieldMapper> availableFields);
}
