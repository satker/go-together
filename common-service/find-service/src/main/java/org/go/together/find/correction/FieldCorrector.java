package org.go.together.find.correction;

import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.mapper.FieldMapperUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.go.together.find.utils.FindUtils.getParsedFields;
import static org.go.together.find.utils.FindUtils.getSingleGroupFields;

public class FieldCorrector {
    private Map<String, String> oldNewValue;
    private final FilterCorrector filterCorrector;
    public FieldCorrector(FilterCorrector filterCorrector){
        this.filterCorrector = filterCorrector;
    }

    public Map<String, String> getOldNewValue() {
        return oldNewValue;
    }

    public String getCorrectedField(Map<String, FieldMapper> fieldMappers, String filterField) {
        oldNewValue = Stream.of(getSingleGroupFields(filterField))
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        oldNewValue.keySet().forEach(searchField -> correctFilterFields(fieldMappers, searchField));
        return getCorrectedFilterFields(filterField);
    }

    private void correctFilterFields(Map<String, FieldMapper> fieldMappers, String searchField) {
        String[] splitByDotString = getParsedFields(searchField);
        if (splitByDotString.length > 1 && fieldMappers.containsKey(splitByDotString[0])) {
            putCorrectedComplexGroupFilterField(fieldMappers.get(splitByDotString[0]), oldNewValue, searchField, splitByDotString);
        } else if (fieldMappers.containsKey(searchField)) {
            oldNewValue.put(searchField, fieldMappers.get(searchField).getCurrentServiceField());
        } else {
            throw new IncorrectFindObject("Field " + searchField + " is unavailable for search.");
        }
    }

    private String getCorrectedFilterFields(String filterField) {
       return oldNewValue.entrySet().stream()
                .map(entry -> Function.identity().andThen(r -> String.valueOf(r).replaceAll(entry.getKey(), entry.getValue())))
                .reduce(Function::andThen)
                .map(objectStringFunction -> objectStringFunction.apply(filterField))
                .orElse(filterField);
    }

    private void putCorrectedComplexGroupFilterField(FieldMapper fieldMapper,
                                                     Map<String, String> singleGroupFields,
                                                     String key,
                                                     String[] splitByDotString) {
        if (fieldMapper.getInnerService() != null) {
            String changedField = getCorrectedComplexField(splitByDotString, fieldMapper);
            singleGroupFields.put(key, changedField);
        } else {
            throw new IncorrectFindObject("Field " + key + " is unavailable for search.");
        }
    }

    private String getCorrectedComplexField(String[] splitByDotString, FieldMapper fieldMapper) {
        String changedField = fieldMapper.getCurrentServiceField();
        List<String> fieldsForChange = List.of(splitByDotString).subList(1, splitByDotString.length);
        FieldDto fieldDto = FieldMapperUtils.getFieldDto(String.join(".", fieldsForChange));
        Map<String, FieldMapper> innerServiceMappingFields = fieldMapper.getInnerService().getMappingFields();
        FieldDto updatedFieldDto = filterCorrector.getCorrectedFieldDto(fieldDto, innerServiceMappingFields,
                new FieldCorrector(filterCorrector));
        return changedField + "." + updatedFieldDto.getLocalField();
    }
}
