package org.go.together.find.correction;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.FilterDto;

import java.util.Map;

public interface CorrectedService {
    Pair<Map<String, FilterDto>, Map<String, FilterDto>> getRemoteAndCorrectedFilters(Map<String, FilterDto> filters,
                                                                                      Map<String, FieldMapper> availableFields);
}
