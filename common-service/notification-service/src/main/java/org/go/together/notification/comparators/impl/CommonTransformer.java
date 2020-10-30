package org.go.together.notification.comparators.impl;

import org.go.together.dto.ComparingObject;
import org.go.together.exceptions.IncorrectDtoException;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.Dto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.comparators.interfaces.Transformer;
import org.go.together.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CommonTransformer implements Transformer {
    private Map<Class<?>, Comparator> classComparatorMap;

    @Autowired
    public void setClassComparatorMap(List<Comparator> comparators) {
        this.classComparatorMap = comparators.stream()
                .collect(Collectors.toMap(comparator -> ReflectionUtils.getParametrizedInterface(comparator.getClass(), 0),
                        Function.identity()));
    }

    @Override
    public Map<String, Object> transform(String fieldName, Object originalObject, Object changedObject, ComparingObject fieldProperties) {
        if (!fieldProperties.getIsDeepCompare() || fieldProperties.getIgnored()) {
            return getComparator(Object.class).compare(fieldName, originalObject, changedObject, fieldProperties);
        } else if (originalObject == null || changedObject == null || originalObject.getClass() == changedObject.getClass()) {
            if (originalObject instanceof Dto && changedObject instanceof Dto) {
                ComparableDto comparableDtoObject = (ComparableDto) originalObject;
                ComparableDto anotherComparableDtoObject = (ComparableDto) changedObject;
                if (fieldProperties.getIdCompare()) {
                    return getComparator(Object.class)
                            .compare(fieldName, comparableDtoObject.getId(), anotherComparableDtoObject.getId(), fieldProperties);
                }
            }
            return getComparator(fieldProperties.getClazz()).compare(fieldName, originalObject, changedObject, fieldProperties);
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
