package org.go.together.correction;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.FieldMapper;
import org.go.together.dto.FilterDto;

import java.util.Map;

public interface CorrectedService {
    Pair<Map<String, FilterDto>, Map<String, FilterDto>> getRemoteAndCorrectedFilters(Map<String, FilterDto> filters,
                                                                                      Map<String, FieldMapper> availableFields);
}
