package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.ComparingObject;
import org.go.together.exceptions.IncorrectDtoException;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.Dto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DtoComparator<T extends ComparableDto> implements Comparator<T> {
    private Map<Class<?>, Comparator> classComparatorMap;

    @Autowired
    public void setClassComparatorMap(List<Comparator> comparators) {
        this.classComparatorMap = comparators.stream()
                .collect(Collectors.toMap(comparator -> ReflectionUtils.getParametrizedInterface(comparator.getClass(), 0),
                        Function.identity()));
    }

    public String compare(String fieldName, T originalObject, T changedObject, boolean idCompare) {
        Set<String> strings = compareDtoFields(originalObject, changedObject);
        String mainField = changedObject.getMainField();
        return getCompareResult(fieldName, strings, mainField);
    }

    private String getCompareResult(String fieldName, Set<String> strings, String mainField) {
        Set<String> result = new HashSet<>();

        if (!strings.isEmpty()) {
            StringBuilder resultString = new StringBuilder()
                    .append(CHANGED_WITH_UPPER_LETTER)
                    .append(StringUtils.SPACE)
                    .append(fieldName);
            if (StringUtils.isNotBlank(mainField)) {
                resultString.append(mainField);
            }
            resultString.append(COLON)
                    .append(StringUtils.join(strings, COMMA));
            result.add(resultString.toString());
        }
        return String.join(". ", result);
    }

    private Set<String> compareDtoFields(T originalObject, T changedObject) {
        return originalObject.getComparingMap().entrySet().stream()
                .map(originalObjectEntry -> getFieldCompareResult(originalObject, changedObject, originalObjectEntry))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }

    private String getFieldCompareResult(T originalObject, T changedObject, Map.Entry<String, ComparingObject> originalObjectEntry) {
        String fieldName = originalObjectEntry.getKey();
        ComparingObject field = originalObjectEntry.getValue();
        ComparingObject comparingField = changedObject.getComparingMap().get(fieldName);
        Function<ComparableDto, Object> fieldValueGetter = comparingField.getFieldValueGetter();
        Object changedFieldValue = fieldValueGetter.apply(changedObject);
        Object originalFieldValue = fieldValueGetter.apply(originalObject);
        if (changedFieldValue != null || originalFieldValue != null) {
            return updateResult(fieldName, originalFieldValue, changedFieldValue, field);
        }
        return StringUtils.EMPTY;
    }

    private String updateResult(String fieldName,
                                Object originalObject,
                                Object changedObject,
                                ComparingObject fieldProperties) {
        if (!fieldProperties.getIsDeepCompare() || fieldProperties.getIgnored()) {
            return getComparator(Object.class).compare(fieldName, originalObject, changedObject, fieldProperties.getIdCompare());
        } else if (originalObject.getClass() == changedObject.getClass()) {
            if (originalObject instanceof Dto && changedObject instanceof Dto) {
                ComparableDto comparableDtoObject = (ComparableDto) originalObject;
                ComparableDto anotherComparableDtoObject = (ComparableDto) changedObject;
                if (fieldProperties.getIdCompare()) {
                    return getComparator(Object.class)
                            .compare(fieldName, comparableDtoObject.getId(), anotherComparableDtoObject.getId(), true);
                }
            }
            if (originalObject instanceof ComparableDto) {
                return compare(fieldName, (T) originalObject, (T) changedObject, fieldProperties.getIdCompare());
            } else {
                return getComparator(originalObject.getClass()).compare(fieldName, originalObject, changedObject, fieldProperties.getIdCompare());
            }
        }
        throw new IncorrectDtoException("Incorrect field type: " + fieldName);
    }

    private Comparator getComparator(Class<?> clazz) {
        return classComparatorMap.entrySet().stream()
                .filter(entry -> entry.getKey() != Object.class)
                .filter(entry -> entry.getKey() == clazz || entry.getKey().isAssignableFrom(clazz))
                .map(Map.Entry::getValue)
                .findFirst().orElse(classComparatorMap.get(Object.class));
    }
}
