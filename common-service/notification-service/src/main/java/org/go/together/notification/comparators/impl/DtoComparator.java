package org.go.together.notification.comparators.impl;

import org.go.together.compare.FieldProperties;
import org.go.together.dto.Dto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.comparemapper.interfaces.CompareMapper;
import org.go.together.utils.NotificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class DtoComparator<T extends Dto> implements Comparator<T> {
    private static final String ID_COMPARE_FIELD = "id_compare";

    private CompareMapper transformer;
    private final Map<Class<? extends Dto>, Map<String, FieldProperties>> classFieldProperties = new HashMap<>();

    @Autowired
    public void setTransformer(CompareMapper transformer) {
        this.transformer = transformer;
    }

    public Map<String, Object> compare(String fieldName, T originalObject, T changedObject, FieldProperties fieldProperties) {
        Map<String, Object> resultMap = compareDtoFields(originalObject, changedObject, fieldProperties);
        return getCompareResult(fieldName, resultMap, fieldProperties);
    }

    private Map<String, FieldProperties> getFieldsProperties(Class<? extends Dto> clazz) {
        Map<String, FieldProperties> comparingObject = classFieldProperties.get(clazz);
        if (comparingObject == null) {
            Map<String, FieldProperties> comparingMap = NotificationUtils.getComparingMap(clazz);
            classFieldProperties.put(clazz, comparingMap);
            return comparingMap;
        }
        return comparingObject;
    }

    private Map<String, Object> getCompareResult(String fieldName,
                                                 Map<String, Object> resultMap,
                                                 FieldProperties fieldProperties) {
        if (!resultMap.isEmpty()) {
            if (isIdCompare(fieldProperties)) {
                Object idCompareResult = resultMap.get(ID_COMPARE_FIELD);
                return Map.of(fieldName, idCompareResult);
            }
            return Map.of(fieldName, resultMap);
        }
        return Collections.emptyMap();
    }

    private Map<String, Object> compareDtoFields(T originalObject, T changedObject, FieldProperties fieldProperties) {
        Map<String, Object> result = new HashMap<>();
        if (isIdCompare(fieldProperties)) {
            FieldProperties idCompareFieldProperties = fieldProperties.toBuilder().idCompare(false).clazzType(UUID.class).build();
            return transformer.transform(ID_COMPARE_FIELD, originalObject.getId(), changedObject.getId(), idCompareFieldProperties);
        }
        getFieldsProperties(originalObject.getClass()).entrySet().stream()
                .map(originalObjectEntry -> getFieldCompareResult(originalObject, changedObject, originalObjectEntry))
                .forEach(result::putAll);
        return result;
    }

    private boolean isIdCompare(FieldProperties fieldProperties) {
        return Optional.ofNullable(fieldProperties).map(FieldProperties::getIdCompare).orElse(false);
    }

    private Map<String, Object> getFieldCompareResult(T originalObject,
                                                      T changedObject,
                                                      Map.Entry<String, FieldProperties> originalObjectEntry) {
        String fieldName = originalObjectEntry.getKey();
        try {
            FieldProperties field = originalObjectEntry.getValue();
            FieldProperties comparingField = getFieldsProperties(changedObject.getClass()).get(fieldName);
            Function<Dto, Object> fieldValueGetter = comparingField.getFieldValueGetter();
            Object changedFieldValue = fieldValueGetter.apply(changedObject);
            Object originalFieldValue = fieldValueGetter.apply(originalObject);
            if (changedFieldValue != null || originalFieldValue != null) {
                return transformer.transform(fieldName, originalFieldValue, changedFieldValue, field);
            }
            return Collections.emptyMap();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot compare field " + fieldName + " and two classes " + changedObject.getClass());
        }
    }
}
