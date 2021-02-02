package org.go.together.notification.comparemapper.impl;

import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingObject;
import org.go.together.dto.Dto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.comparators.transformers.interfaces.Transformer;
import org.go.together.notification.comparemapper.interfaces.CompareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.go.together.utils.ReflectionUtils.getClazz;

@Component
public class CommonCompareMapper implements CompareMapper {
    private Transformer<Comparator> compareTransformer;

    @Autowired
    public void setCompareTransformer(Transformer<Comparator> compareTransformer) {
        this.compareTransformer = compareTransformer;
    }

    @Override
    public Map<String, Object> transform(String fieldName, Object originalObject, Object changedObject, ComparingObject fieldProperties) {
        Class<?> clazz = getClazz(fieldProperties.getClazzType());
        if (fieldProperties.getIdCompare()) {
            if (ComparableDto.class.isAssignableFrom(clazz)) {
                return getIdCompareDto(fieldName, originalObject, changedObject, fieldProperties);
            }
            if (Collection.class.isAssignableFrom(clazz)) {
                return getIdCompareCollection(fieldName, originalObject, changedObject, fieldProperties);
            }
        }
        return compareTransformer.get(fieldName, originalObject, changedObject, fieldProperties)
                .compare(fieldName, originalObject, changedObject, fieldProperties);
    }

    private Map<String, Object> getIdCompareCollection(String fieldName, Object originalObject, Object changedObject,
                                                       ComparingObject fieldProperties) {
        ComparingObject newComparingObject = fieldProperties.toBuilder().clazzType(Collection.class).idCompare(false).build();
        Collection<UUID> comparableCollectionDtoIds = ((Collection<Dto>) originalObject).stream()
                .map(Dto::getId)
                .collect(Collectors.toSet());
        Collection<UUID> anotherCollectionDtoIds = ((Collection<Dto>) changedObject).stream()
                .map(Dto::getId)
                .collect(Collectors.toSet());
        return compareTransformer.get(fieldName, originalObject, changedObject, newComparingObject)
                .compare(fieldName, comparableCollectionDtoIds, anotherCollectionDtoIds, newComparingObject);
    }

    private Map<String, Object> getIdCompareDto(String fieldName, Object originalObject,
                                                Object changedObject, ComparingObject fieldProperties) {
        ComparingObject newComparingObject = fieldProperties.toBuilder().clazzType(UUID.class).idCompare(false).build();
        ComparableDto comparableDtoObject = (ComparableDto) originalObject;
        ComparableDto anotherComparableDtoObject = (ComparableDto) changedObject;
        return compareTransformer.get(fieldName, originalObject, changedObject, newComparingObject)
                .compare(fieldName, comparableDtoObject.getId(), anotherComparableDtoObject.getId(), newComparingObject);
    }
}
