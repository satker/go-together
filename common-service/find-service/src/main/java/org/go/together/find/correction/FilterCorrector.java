package org.go.together.find.correction;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.mapper.FieldMapperUtils;
import org.go.together.find.utils.FindUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class FilterCorrector implements CorrectedService {
    public Pair<Map<FieldDto, FilterDto>, Map<FieldDto, FilterDto>> getRemoteAndCorrectedFilters(Map<String, FilterDto> filters,
                                                                                             Map<String, FieldMapper> availableFields) {
        FieldCorrector fieldCorrector = new FieldCorrector(this);
        Map<FieldDto, FilterDto> localFilters = new HashMap<>();
        Map<FieldDto, FilterDto> remoteFilters = new HashMap<>();
        filters.forEach((key, value) -> {
            FieldDto fieldDto = FieldMapperUtils.getFieldDto(key);
            Map<String, FieldMapper> fieldMappers = FindUtils.getFieldMappers(availableFields, fieldDto);
            boolean isNotRemote = fieldMappers.values().stream()
                    .allMatch(fieldMapper -> fieldMapper.getRemoteServiceClient() == null);
            FieldDto localFieldForSearch = getCorrectedFieldDto(fieldDto, fieldMappers, fieldCorrector);
            if (isNotRemote) {
                Collection<Map<String, Object>> correctedValuesForSearch =
                        getCorrectedValues(fieldCorrector.getOldNewValue(), value.getValues());
                value.setValues(correctedValuesForSearch);
                localFilters.put(localFieldForSearch, value);
            } else {
                remoteFilters.put(localFieldForSearch, value);
            }
        });
        return Pair.of(remoteFilters, localFilters);
    }

    private Collection<Map<String, Object>> getCorrectedValues(Map<String, String> fieldMappers,
                                                               Collection<Map<String, Object>> filters) {
        Collection<Map<String, Object>> result = new HashSet<>();
        for (Map<String, Object> next : filters) {
            Map<String, Object> map = new HashMap<>();
            fieldMappers.forEach((oldKey, newKey) -> {
                map.put(newKey, next.get(oldKey));
            });
            result.add(map);
        }
        return result;
    }

    public FieldDto getCorrectedFieldDto(FieldDto fieldDto,
                                         Map<String, FieldMapper> fieldMappers,
                                         FieldCorrector fieldCorrector) {
        String[] localEntityFullFields = fieldDto.getPaths();

        PathCorrector pathCorrector = new PathCorrector();
        StringBuilder correctedPath = pathCorrector.getCorrectedPath(localEntityFullFields, fieldMappers);
        Map<String, FieldMapper> lastFieldMapper = pathCorrector.getCurrentFieldMapper();

        String lastFilterFields = fieldDto.getFilterFields();
        String localEntityCorrectChildField = fieldCorrector.getCorrectedField(lastFieldMapper, lastFilterFields);

        return getCorrectedFieldDto(fieldDto, correctedPath, localEntityCorrectChildField);
    }

    private FieldDto getCorrectedFieldDto(FieldDto fieldDto,
                                          StringBuilder resultFilterString,
                                          String correctedFilterFields) {
        FieldDto.FieldDtoBuilder fieldDtoBuilder = FieldDto.builder();

        if (resultFilterString.length() > 0) {
            resultFilterString.append(".").append(correctedFilterFields);
        } else {
            resultFilterString.append(correctedFilterFields);
        }
        fieldDtoBuilder.localField(resultFilterString.toString());

        String remoteField = fieldDto.getRemoteField();
        if (StringUtils.isNotBlank(remoteField)) {
            fieldDtoBuilder.remoteField(remoteField);
        }
        return fieldDtoBuilder.build();
    }
}
