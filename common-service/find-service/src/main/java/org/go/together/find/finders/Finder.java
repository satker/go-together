package org.go.together.find.finders;

import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.FilterDto;

import java.util.Collection;
import java.util.Map;

public interface Finder {
    Map<String, Collection<Object>> getFilters(Map<String, FilterDto> filters,
                                               Map<String, FieldMapper> availableFields);
}
