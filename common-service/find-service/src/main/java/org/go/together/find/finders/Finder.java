package org.go.together.find.finders;

import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;

import java.util.Collection;
import java.util.Map;

public interface Finder {
    Map<FieldDto, Collection<Object>> getFilters(Map<FieldDto, FilterDto> filters,
                                               Map<String, FieldMapper> availableFields);
}
