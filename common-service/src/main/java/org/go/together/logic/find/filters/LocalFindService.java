package org.go.together.logic.find.filters;

import org.go.together.dto.filter.FilterDto;
import org.go.together.logic.find.FieldMapper;
import org.go.together.logic.find.FieldParser;

import java.util.HashMap;
import java.util.Map;

public class LocalFindService implements Filter<FilterDto> {
    public Map<String, FilterDto> getFilters(Map<String, FilterDto> filters,
                                             Map<String, FieldMapper> availableFields) {
        Map<String, FilterDto> currentFilters = new HashMap<>();
        filters.forEach((key, value) -> {
            FieldMapper fieldMapper = FieldParser.getFieldMapper(availableFields, key);
            if (fieldMapper.getRemoteServiceName() == null) {
                currentFilters.put(fieldMapper.getCurrentServiceField(), value);
            }
        });
        return currentFilters;
    }
}
