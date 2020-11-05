package org.go.together.notification.comparators.impl;

import org.go.together.compare.ComparingObject;
import org.go.together.dto.Dto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.transport.interfaces.CompareTransport;
import org.go.together.utils.NotificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DtoComparator<T extends Dto> implements Comparator<T> {
    private CompareTransport transformer;
    private final Map<Class<? extends Dto>, Map<String, ComparingObject>> classFieldProperties =
            new HashMap<>();

    @Autowired
    public void setTransformer(CompareTransport transformer) {
        this.transformer = transformer;
    }

    public Map<String, Object> compare(String fieldName, T originalObject, T changedObject, ComparingObject fieldProperties) {
        Map<String, Object> resultMap = compareDtoFields(originalObject, changedObject);
        return getCompareResult(fieldName, resultMap);
    }

    private Map<String, ComparingObject> getFieldsProperties(Class<? extends Dto> clazz) {
        Map<String, ComparingObject> comparingObject = classFieldProperties.get(clazz);
        if (comparingObject == null) {
            Map<String, ComparingObject> comparingMap = NotificationUtils.getComparingMap(clazz);
            classFieldProperties.put(clazz, comparingMap);
            return comparingMap;
        }
        return comparingObject;
    }

    private Map<String, Object> getCompareResult(String fieldName,
                                                 Map<String, Object> resultMap) {
        if (!resultMap.isEmpty()) {
            return Map.of(fieldName, resultMap);
        }
        return Collections.emptyMap();
    }

    private Map<String, Object> compareDtoFields(T originalObject, T changedObject) {
        Map<String, Object> result = new HashMap<>();
        getFieldsProperties(originalObject.getClass()).entrySet().stream()
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
        ComparingObject comparingField = getFieldsProperties(changedObject.getClass()).get(fieldName);
        Function<Dto, Object> fieldValueGetter = comparingField.getFieldValueGetter();
        Object changedFieldValue = fieldValueGetter.apply(changedObject);
        Object originalFieldValue = fieldValueGetter.apply(originalObject);
        if (changedFieldValue != null || originalFieldValue != null) {
            return transformer.transform(fieldName, originalFieldValue, changedFieldValue, field);
        }
        return Collections.emptyMap();
    }
}
