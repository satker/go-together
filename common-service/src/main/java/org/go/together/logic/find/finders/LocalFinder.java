package org.go.together.logic.find.finders;

import org.go.together.dto.filter.FieldMapper;
import org.go.together.dto.filter.FilterDto;
import org.go.together.logic.find.utils.FieldParser;

import java.util.Map;
import java.util.stream.Collectors;

public class LocalFinder implements Finder<FilterDto> {
    public Map<String, FilterDto> getFilters(Map<String, FilterDto> filters,
                                             Map<String, FieldMapper> availableFields) {
        return filters.entrySet().stream()
                .filter(entry -> FieldParser.getFieldMapper(availableFields, entry.getKey()).getRemoteServiceClient() == null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
