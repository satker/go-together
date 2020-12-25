package org.go.together.find.correction.field;

import org.go.together.compare.FieldMapper;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.correction.field.dto.CorrectedFieldDto;
import org.go.together.find.correction.fieldpath.FieldPathCorrector;
import org.go.together.find.dto.FieldDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.go.together.find.utils.FindUtils.getParsedFields;
import static org.go.together.find.utils.FindUtils.getSingleGroupFields;

@Component
public class FieldCorrectorService implements FieldCorrector {
    private FieldPathCorrector fieldPathCorrector;

    @Autowired
    public void setFieldPathCorrector(FieldPathCorrector fieldPathCorrector) {
        this.fieldPathCorrector = fieldPathCorrector;
    }

    public CorrectedFieldDto correct(Map<String, FieldMapper> fieldMappers, FieldDto fieldDto) {
        String filterField = fieldDto.getFilterFields();
        Map<String, String> oldNewFilterField = Stream.of(getSingleGroupFields(filterField))
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        Map<String, Class<?>> oldValueClass = new HashMap<>();
        Map<String, String> filledOldNewFilterField = oldNewFilterField.keySet().stream()
                .map(searchField -> correctFilterFields(fieldMappers, searchField, oldValueClass))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        String correctedFilterFields = getCorrectedFilterFields(filterField, filledOldNewFilterField);
        return CorrectedFieldDto.builder().correctedField(correctedFilterFields)
                .oldNewFilterField(filledOldNewFilterField)
                .oldValueClass(oldValueClass).build();
    }

    private Map.Entry<String, String> correctFilterFields(Map<String, FieldMapper> fieldMappers,
                                                          String searchField,
                                                          Map<String, Class<?>> oldValueClass) {
        String[] splitByDotString = getParsedFields(searchField);
        if (splitByDotString.length > 1 && fieldMappers.containsKey(splitByDotString[0])) {
            Map.Entry<String, String> correctedComplexGroupFilterField =
                    getCorrectedComplexGroupFilterField(fieldMappers.get(splitByDotString[0]), searchField, splitByDotString);
            return Map.entry(correctedComplexGroupFilterField.getKey(), correctedComplexGroupFilterField.getValue());
        } else if (fieldMappers.containsKey(searchField)) {
            FieldMapper fieldMapper = fieldMappers.get(searchField);
            oldValueClass.put(searchField, fieldMapper.getFieldClass());
            return Map.entry(searchField, fieldMapper.getCurrentServiceField());
        } else {
            throw new IncorrectFindObject("Field " + searchField + " is unavailable for search.");
        }
    }

    private String getCorrectedFilterFields(String filterField, Map<String, String> filledOldNewFilterField) {
        return filledOldNewFilterField.entrySet().stream()
                .map(entry -> Function.identity().andThen(r -> String.valueOf(r).replaceAll(entry.getKey(), entry.getValue())))
                .reduce(Function::andThen)
                .map(objectStringFunction -> objectStringFunction.apply(filterField))
                .orElse(filterField);
    }

    private Map.Entry<String, String> getCorrectedComplexGroupFilterField(FieldMapper fieldMapper,
                                                                          String oldKey,
                                                                          String[] splitByDotString) {
        if (fieldMapper.getInnerService() != null) {
            String changedField = getCorrectedComplexField(splitByDotString, fieldMapper);
            return Map.entry(oldKey, changedField);
        } else {
            throw new IncorrectFindObject("Field " + oldKey + " is unavailable for search.");
        }
    }

    private String getCorrectedComplexField(String[] splitByDotString, FieldMapper fieldMapper) {
        String changedField = fieldMapper.getCurrentServiceField();
        List<String> fieldsForChange = List.of(splitByDotString).subList(1, splitByDotString.length);
        FieldDto fieldDto = new FieldDto(String.join(".", fieldsForChange));
        Map<String, FieldMapper> innerServiceMappingFields = fieldMapper.getInnerService().getMappingFields();
        CorrectedFieldDto updatedFieldDto = fieldPathCorrector.getCorrectedFieldDto(fieldDto, innerServiceMappingFields);
        return changedField + "." + updatedFieldDto.getFieldDto().getLocalField();
    }
}
