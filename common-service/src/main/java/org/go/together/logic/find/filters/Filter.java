package org.go.together.logic.find.filters;

import org.go.together.dto.filter.FilterDto;
import org.go.together.logic.find.FieldMapper;

import java.util.Map;

public interface Filter<T> {
    Map<String, T> getFilters(Map<String, FilterDto> filters,
                              Map<String, FieldMapper> availableFields);
}
