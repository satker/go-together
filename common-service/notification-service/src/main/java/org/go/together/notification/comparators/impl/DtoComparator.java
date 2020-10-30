package org.go.together.notification.comparators.impl;

import lombok.SneakyThrows;
import org.go.together.dto.ComparingObject;
import org.go.together.interfaces.ComparableDto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.comparators.interfaces.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DtoComparator<T extends ComparableDto> implements Comparator<T> {
    private Transformer transformer;

    @Autowired
    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

    public Map<String, Object> compare(String fieldName, T originalObject, T changedObject, ComparingObject fieldProperties) {
        Map<String, Object> resultMap = compareDtoFields(originalObject, changedObject);
        String mainField = changedObject.getMainField();
        return getCompareResult(fieldName, resultMap, mainField);
    }

    @SneakyThrows
    private Map<String, Object> getCompareResult(String fieldName,
                                                 Map<String, Object> resultMap,
                                                 String mainField) {
        if (!resultMap.isEmpty()) {
            return Map.of(fieldName + mainField, resultMap);
        }
        return Collections.emptyMap();
    }

    private Map<String, Object> compareDtoFields(T originalObject, T changedObject) {
        Map<String, Object> result = new HashMap<>();
        originalObject.getComparingMap().entrySet().stream()
                .map(originalObjectEntry -> getFieldCompareResult(originalObject, changedObject, originalObjectEntry))
                .filter(map -> !map.isEmpty())
                .forEach(result::putAll);
        return result;
    }

    private Map<String, Object> getFieldCompareResult(T originalObject,
                                                      T changedObject,
                                                      Map.Entry<String, ComparingObject> originalObjectEntry) {
        String fieldName = originalObjectEntry.getKey();
        ComparingObject field = originalObjectEntry.getValue();
        ComparingObject comparingField = changedObject.getComparingMap().get(fieldName);
        Function<ComparableDto, Object> fieldValueGetter = comparingField.getFieldValueGetter();
        Object changedFieldValue = fieldValueGetter.apply(changedObject);
        Object originalFieldValue = fieldValueGetter.apply(originalObject);
        if (changedFieldValue != null || originalFieldValue != null) {
            return transformer.transform(fieldName, originalFieldValue, changedFieldValue, field);
        }
        return Collections.emptyMap();
    }
}
