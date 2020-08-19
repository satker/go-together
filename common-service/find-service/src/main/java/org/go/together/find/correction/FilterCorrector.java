package org.go.together.find.correction;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.correction.field.CorrectedFieldDto;
import org.go.together.find.correction.field.FieldCorrector;
import org.go.together.find.correction.path.CorrectedPathDto;
import org.go.together.find.correction.path.PathCorrector;
import org.go.together.find.correction.values.ValuesCorrector;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.mapper.FieldMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
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

    public Pair<Map<FieldDto, FilterDto>, Map<FieldDto, FilterDto>> getRemoteAndCorrectedFilters(Map<String, FilterDto> filters,
                                                                                                 Map<String, FieldMapper> availableFields) {
        Map<FieldDto, FilterDto> localFilters = new HashMap<>();
        Map<FieldDto, FilterDto> remoteFilters = new HashMap<>();
        filters.forEach((key, value) -> {
            FieldDto fieldDto = FieldMapperUtils.getFieldDto(key);
            Map<String, FieldMapper> fieldMappers = getFieldMappersByFieldDto(availableFields, fieldDto);
            boolean isNotRemote = fieldMappers.values().stream()
                    .allMatch(fieldMapper -> fieldMapper.getRemoteServiceClient() == null);
            CorrectedFieldDto localFieldForSearch = getCorrectedFieldDto(fieldDto, fieldMappers);
            if (isNotRemote) {
                Collection<Map<String, Object>> correctedValuesForSearch =
                        valuesCorrector.getCorrectedValues(localFieldForSearch, value.getValues());
                value.setValues(correctedValuesForSearch);
                localFilters.put(localFieldForSearch.getFieldDto(), value);
            } else {
                remoteFilters.put(localFieldForSearch.getFieldDto(), value);
            }
        });
        return Pair.of(remoteFilters, localFilters);
    }

    public CorrectedFieldDto getCorrectedFieldDto(FieldDto fieldDto,
                                                  Map<String, FieldMapper> fieldMappers) {
        String[] localEntityFullFields = fieldDto.getPaths();

        CorrectedPathDto correctedPath = pathCorrector.getCorrectedPath(localEntityFullFields, fieldMappers);
        Map<String, FieldMapper> lastFieldMapper = correctedPath.getCurrentFieldMapper();

        String lastFilterFields = fieldDto.getFilterFields();
        CorrectedFieldDto correctedField = fieldCorrector.getCorrectedField(lastFieldMapper, lastFilterFields);

        FieldDto correctedFieldDto = getCorrectedFieldDto(fieldDto, correctedPath.getCorrectedPath(), correctedField.getCorrectedField());

        return correctedField.toBuilder().fieldDto(correctedFieldDto).build();
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
