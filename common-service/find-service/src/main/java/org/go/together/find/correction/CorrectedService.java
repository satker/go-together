package org.go.together.find.correction;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;

import java.util.Map;

public interface CorrectedService {
    Pair<Map<FieldDto, FilterDto>, Map<FieldDto, FilterDto>> getRemoteAndCorrectedFilters(Map<String, FilterDto> filters,
                                                                                        Map<String, FieldMapper> availableFields);
}
