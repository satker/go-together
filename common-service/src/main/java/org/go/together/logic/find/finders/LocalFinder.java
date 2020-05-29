package org.go.together.logic.find.finders;

import org.go.together.dto.filter.FieldMapper;
import org.go.together.dto.filter.FilterDto;
import org.go.together.logic.find.utils.FieldParser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.go.together.logic.find.utils.FieldParser.getCorrectedLocalFieldForSearch;

public class LocalFinder implements Finder<FilterDto> {
    public Map<String, FilterDto> getFilters(Map<String, FilterDto> filters,
                                             Map<String, FieldMapper> availableFields) {
        Map<String, FilterDto> result = new HashMap<>();
        filters.forEach((key, value) -> {
            Map<String, FieldMapper> fieldMappers = FieldParser.getFieldMappers(availableFields, key);
            boolean isNotRemote = fieldMappers.values().stream()
                    .allMatch(fieldMapper -> fieldMapper.getRemoteServiceClient() == null);
            if (isNotRemote) {
                String localFieldForSearch = getCorrectedLocalFieldForSearch(fieldMappers, key);
                Collection<Map<String, Object>> correctedLocalFieldForSearch =
                        getCorrectedLocalFieldForSearch(fieldMappers, value.getValues());
                value.setValues(correctedLocalFieldForSearch);
                result.put(localFieldForSearch, value);
            }
        });
        return result;
    }
}
