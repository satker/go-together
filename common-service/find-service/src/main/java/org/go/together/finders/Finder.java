package org.go.together.finders;

import org.go.together.dto.FieldMapper;
import org.go.together.dto.FilterDto;

import java.util.Collection;
import java.util.Map;

public interface Finder {
    Map<String, Collection<Object>> getFilters(Map<String, FilterDto> filters,
                                               Map<String, FieldMapper> availableFields);
}
