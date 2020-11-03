package org.go.together.find.correction;

import org.go.together.dto.form.FilterDto;
import org.go.together.find.correction.field.dto.CorrectedFieldDto;
import org.go.together.find.correction.fieldpath.FieldPathCorrector;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component("remoteFilters")
public class CorrectorRemoteFiltersService implements CorrectorService {
    private final FieldPathCorrector fieldPathCorrector;

    public CorrectorRemoteFiltersService(FieldPathCorrector fieldPathCorrector) {
        this.fieldPathCorrector = fieldPathCorrector;
    }

    @Override
    public Map<FieldDto, FilterDto> getCorrectedFilters(Map<String, FilterDto> filters,
                                                        Map<String, FieldMapper> availableFields) {
        return filters.entrySet().stream()
                .map(entry -> Map.entry(new FieldDto(entry.getKey()), entry.getValue()))
                .filter(entry -> entry.getKey().getFieldMappersByFieldDto(availableFields).values().stream()
                        .noneMatch(fieldMapper -> fieldMapper.getRemoteServiceClient() == null))
                .map(entry -> getRemoteFields(entry, entry.getKey().getFieldMappersByFieldDto(availableFields)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<FieldDto, FilterDto> getRemoteFields(Map.Entry<FieldDto, FilterDto> entry,
                                                           Map<String, FieldMapper> fieldMappers) {
        CorrectedFieldDto localFieldForSearch = fieldPathCorrector.getCorrectedFieldDto(entry.getKey(), fieldMappers);
        return Map.entry(localFieldForSearch.getFieldDto(), entry.getValue());
    }
}
