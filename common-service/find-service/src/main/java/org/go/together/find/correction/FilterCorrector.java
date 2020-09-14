package org.go.together.find.correction;

import org.apache.commons.lang3.StringUtils;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.correction.field.CorrectedFieldDto;
import org.go.together.find.correction.field.FieldCorrector;
import org.go.together.find.correction.path.CorrectedPathDto;
import org.go.together.find.correction.path.PathCorrector;
import org.go.together.find.correction.values.ValuesCorrector;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.go.together.find.utils.FindUtils.getParsedFields;
import static org.go.together.find.utils.FindUtils.getSingleGroupFields;

@Component
public class FilterCorrector implements CorrectedService {
    private ValuesCorrector valuesCorrector;
    private FieldCorrector fieldCorrector;
    private PathCorrector pathCorrector;

    @Autowired
    public void setFieldCorrector(FieldCorrector fieldCorrector) {
        this.fieldCorrector = fieldCorrector;
    }

    @Autowired
    private void setValuesCorrector(ValuesCorrector valuesCorrector) {
        this.valuesCorrector = valuesCorrector;
    }

    @Autowired
    private void setPathCorrector(PathCorrector pathCorrector) {
        this.pathCorrector = pathCorrector;
    }

    @Override
    public Map<FieldDto, FilterDto> getRemoteFilters(Map<String, FilterDto> filters,
                                                     Map<String, FieldMapper> availableFields) {
        return filters.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(new FieldDto(entry.getKey()), entry.getValue()))
                .filter(entry -> getFieldMappersByFieldDto(availableFields, entry.getKey()).values().stream()
                        .noneMatch(fieldMapper -> fieldMapper.getRemoteServiceClient() == null))
                .map(entry -> getRemoteFields(entry, getFieldMappersByFieldDto(availableFields, entry.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<FieldDto, FilterDto> getLocalFilters(Map<String, FilterDto> filters,
                                                    Map<String, FieldMapper> availableFields) {
        return filters.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(new FieldDto(entry.getKey()), entry.getValue()))
                .filter(entry -> getFieldMappersByFieldDto(availableFields, entry.getKey()).values().stream()
                        .allMatch(fieldMapper -> fieldMapper.getRemoteServiceClient() == null))
                .map(entry -> getCorrectedLocalFields(entry, getFieldMappersByFieldDto(availableFields, entry.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (filterDto, filterDto1) -> filterDto));
    }

    private Map.Entry<FieldDto, FilterDto> getRemoteFields(Map.Entry<FieldDto, FilterDto> entry,
                                                           Map<String, FieldMapper> fieldMappers) {
        CorrectedFieldDto localFieldForSearch = getCorrectedFieldDto(entry.getKey(), fieldMappers);
        return new AbstractMap.SimpleEntry<>(localFieldForSearch.getFieldDto(), entry.getValue());
    }

    private Map.Entry<FieldDto, FilterDto> getCorrectedLocalFields(Map.Entry<FieldDto, FilterDto> entry,
                                                                   Map<String, FieldMapper> fieldMappers) {
        CorrectedFieldDto localFieldForSearch = getCorrectedFieldDto(entry.getKey(), fieldMappers);
        FilterDto value = entry.getValue();
        Collection<Map<String, Object>> correctedValuesForSearch =
                valuesCorrector.correct(localFieldForSearch, value.getValues());
        value.setValues(correctedValuesForSearch);
        return new AbstractMap.SimpleEntry<>(localFieldForSearch.getFieldDto(), value);
    }

    public CorrectedFieldDto getCorrectedFieldDto(FieldDto fieldDto,
                                                  Map<String, FieldMapper> fieldMappers) {
        String[] localEntityFullFields = fieldDto.getPaths();
        CorrectedPathDto correctedPath = pathCorrector.correct(localEntityFullFields, fieldMappers);
        Map<String, FieldMapper> lastFieldMapper = correctedPath.getCurrentFieldMapper();
        String lastFilterFields = fieldDto.getFilterFields();
        CorrectedFieldDto correctedField = fieldCorrector.correct(lastFieldMapper, lastFilterFields);
        FieldDto correctedFieldDto = getCorrectedFieldDto(fieldDto,
                correctedPath.getCorrectedPath(),
                correctedField.getCorrectedField());
        return correctedField.toBuilder().fieldDto(correctedFieldDto).build();
    }

    private FieldDto getCorrectedFieldDto(FieldDto fieldDto,
                                          StringBuilder resultFilterString,
                                          String correctedFilterFields) {
        if (resultFilterString.length() > 0) {
            resultFilterString.append(".").append(correctedFilterFields);
        } else {
            resultFilterString.append(correctedFilterFields);
        }
        String fieldDtoRemoteField = fieldDto.getRemoteField();
        String remoteField = StringUtils.isNotBlank(fieldDtoRemoteField) ? fieldDtoRemoteField : null;
        return new FieldDto(resultFilterString.toString(), remoteField);
    }

    private Map<String, FieldMapper> getFieldMappersByFieldDto(Map<String, FieldMapper> availableFields,
                                                               FieldDto fieldDto) {
        String[] localEntityFullFields = fieldDto.getPaths();

        String localEntityField = localEntityFullFields[0];
        String[] singleGroupFields = getSingleGroupFields(localEntityField);
        try {
            return Stream.of(singleGroupFields)
                    .collect(Collectors.toMap(this::getFirstField,
                            field -> getFieldMapper(availableFields, field),
                            (fieldMapper, fieldMapper2) -> fieldMapper));
        } catch (Exception exception) {
            throw new IncorrectFindObject("Field " + fieldDto.toString() + " is unavailable for search.");
        }
    }

    private FieldMapper getFieldMapper(Map<String, FieldMapper> availableFields,
                                              String field) {
        String firstField = getFirstField(field);
        return availableFields.get(firstField);
    }

    private String getFirstField(String field) {
        if (field.contains(".")) {
            String[] splitByCommaString = getParsedFields(field);
            return splitByCommaString[0];
        }
        return field;
    }
}
