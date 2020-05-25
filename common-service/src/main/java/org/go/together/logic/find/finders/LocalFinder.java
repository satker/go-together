package org.go.together.logic.find.finders;

import org.go.together.dto.filter.FieldMapper;
import org.go.together.dto.filter.FilterDto;
import org.go.together.logic.find.utils.FieldParser;

import java.util.HashMap;
import java.util.Map;

public class LocalFinder implements Finder<FilterDto> {
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
