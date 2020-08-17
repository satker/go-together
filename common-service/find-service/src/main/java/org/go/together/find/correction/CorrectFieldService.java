package org.go.together.find.correction;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.mapper.FieldMapperUtils;
import org.go.together.find.utils.FindUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.go.together.find.utils.FindUtils.*;

@Component
public class CorrectFieldService implements CorrectedService {
    public Pair<Map<FieldDto, FilterDto>, Map<FieldDto, FilterDto>> getRemoteAndCorrectedFilters(Map<String, FilterDto> filters,
                                                                                             Map<String, FieldMapper> availableFields) {
        Map<FieldDto, FilterDto> localFilters = new HashMap<>();
        Map<FieldDto, FilterDto> remoteFilters = new HashMap<>();
        filters.forEach((key, value) -> {
            FieldDto fieldDto = FieldMapperUtils.getFieldDto(key);
            Map<String, FieldMapper> fieldMappers = FindUtils.getFieldMappers(availableFields, fieldDto);
            boolean isNotRemote = fieldMappers.values().stream()
                    .allMatch(fieldMapper -> fieldMapper.getRemoteServiceClient() == null);
            Pair<Map<String, String>, FieldDto> localFieldForSearch =
                    getCorrectedPathAndFields(fieldDto, fieldMappers);
            if (isNotRemote) {
                Collection<Map<String, Object>> correctedValuesForSearch =
                        getCorrectedValues(localFieldForSearch.getLeft(), value.getValues());
                value.setValues(correctedValuesForSearch);
                localFilters.put(localFieldForSearch.getRight(), value);
            } else {
                remoteFilters.put(localFieldForSearch.getRight(), value);
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

    private Pair<Map<String, String>, FieldDto> getCorrectedPathAndFields(FieldDto fieldDto, Map<String, FieldMapper> fieldMappers) {
        String[] localEntityFullFields = fieldDto.getPaths();

        Pair<Map<String, FieldMapper>, StringBuilder> correctedPath = getCorrectedPath(localEntityFullFields, fieldMappers);
        StringBuilder result = new StringBuilder(correctedPath.getRight());

        String lastPathField = localEntityFullFields[localEntityFullFields.length - 1];
        Pair<Map<String, String>, String> localEntityCorrectChildField = getCorrectedField(correctedPath.getLeft(), lastPathField);

        FieldDto.FieldDtoBuilder fieldDtoBuilder = FieldDto.builder();

        if (result.length() > 0) {
            result.append(".").append(localEntityCorrectChildField.getRight());
        } else {
            result.append(localEntityCorrectChildField.getRight());
        }
        fieldDtoBuilder.localField(result.toString());

        String remoteField = fieldDto.getRemoteField();
        if (StringUtils.isNotBlank(remoteField)) {
            fieldDtoBuilder.remoteField(remoteField);
        }
        return Pair.of(localEntityCorrectChildField.getLeft(), fieldDtoBuilder.build());
    }

    private Pair<Map<String, FieldMapper>, StringBuilder> getCorrectedPath(String[] localPaths,
                                                                           Map<String, FieldMapper> fieldMappers) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < localPaths.length - 1; i++) {
            FieldMapper fieldMapper = fieldMappers.get(localPaths[i]);
            result.append(fieldMapper.getCurrentServiceField());
            if (fieldMapper.getInnerService() != null) {
                fieldMappers = fieldMapper.getInnerService().getMappingFields();
            }
        }
        return Pair.of(fieldMappers, result);
    }

    private Pair<Map<String, String>, String> getCorrectedField(Map<String, FieldMapper> fieldMappers, String lastPathField) {
        Map<String, String> singleGroupFields = Stream.of(getSingleGroupFields(lastPathField))
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        singleGroupFields.forEach((key, value) -> {
            String[] splitByDotString = getParsedFields(key);
            if (splitByDotString.length > 1 &&
                    fieldMappers.containsKey(splitByDotString[0])) {
                FieldMapper fieldMapper = fieldMappers.get(splitByDotString[0]);
                if (fieldMapper.getInnerService() != null) {
                    StringBuilder changedField = getCorrectedField(splitByDotString, fieldMapper);
                    singleGroupFields.put(key, changedField.toString());
                } else {
                    throw new IncorrectFindObject("Field " + key + " is unavailable for search.");
                }
            } else if (fieldMappers.containsKey(key)) {
                singleGroupFields.put(key, fieldMappers.get(key).getCurrentServiceField());
            } else {
                throw new IncorrectFindObject("Field " + key + " is unavailable for search.");
            }
        });
        final String[] result = {lastPathField};
        singleGroupFields.forEach((oldValue, newValue) -> {
            result[0] = result[0].replaceAll(oldValue, newValue);
        });
        return Pair.of(singleGroupFields, result[0]);
    }

    private StringBuilder getCorrectedField(String[] splitByDotString, FieldMapper fieldMapper) {
        StringBuilder changedField = new StringBuilder(fieldMapper.getCurrentServiceField());
        List<String> fieldsForChange = List.of(splitByDotString).subList(1, splitByDotString.length);
        FieldDto fieldDto = FieldMapperUtils.getFieldDto(String.join(".", fieldsForChange));
        FieldDto updatedFieldDto = getCorrectedPathAndFields(fieldDto, fieldMapper.getInnerService().getMappingFields()).getRight();
        changedField.append(".").append(updatedFieldDto.getLocalField());
        return changedField;
    }
}
