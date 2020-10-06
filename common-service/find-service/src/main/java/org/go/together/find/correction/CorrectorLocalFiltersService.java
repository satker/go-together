package org.go.together.find.correction;

import org.go.together.find.correction.field.dto.CorrectedFieldDto;
import org.go.together.find.correction.fieldpath.FieldPathCorrector;
import org.go.together.find.correction.values.ValuesCorrector;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component("localFilters")
public class CorrectorLocalFiltersService implements CorrectorService {
    private final ValuesCorrector valuesCorrector;
    private final FieldPathCorrector fieldPathCorrector;

    public CorrectorLocalFiltersService(ValuesCorrector valuesCorrector,
                                        FieldPathCorrector fieldPathCorrector) {
        this.valuesCorrector = valuesCorrector;
        this.fieldPathCorrector = fieldPathCorrector;
    }

    public Map<FieldDto, FilterDto> getCorrectedFilters(Map<String, FilterDto> filters,
                                                        Map<String, FieldMapper> availableFields) {
        return filters.entrySet().stream()
                .map(entry -> Map.entry(new FieldDto(entry.getKey()), entry.getValue()))
                .filter(entry -> entry.getKey().getFieldMappersByFieldDto(availableFields).values().stream()
                        .allMatch(fieldMapper -> fieldMapper.getRemoteServiceClient() == null))
                .map(entry -> getCorrectedLocalFields(entry, entry.getKey().getFieldMappersByFieldDto(availableFields)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (filterDto, filterDto1) -> filterDto));
    }

    private Map.Entry<FieldDto, FilterDto> getCorrectedLocalFields(Map.Entry<FieldDto, FilterDto> entry,
                                                                   Map<String, FieldMapper> fieldMappers) {
        CorrectedFieldDto localFieldForSearch = fieldPathCorrector.getCorrectedFieldDto(entry.getKey(), fieldMappers);
        FilterDto value = entry.getValue();
        Collection<Map<String, Object>> correctedValuesForSearch =
                valuesCorrector.correct(localFieldForSearch, value.getValues());
        value.setValues(correctedValuesForSearch);
        return Map.entry(localFieldForSearch.getFieldDto(), value);
    }
}
