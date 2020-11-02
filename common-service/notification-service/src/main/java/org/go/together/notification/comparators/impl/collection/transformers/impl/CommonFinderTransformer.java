package org.go.together.notification.comparators.impl.collection.transformers.impl;

import org.go.together.dto.ComparingObject;
import org.go.together.notification.comparators.impl.collection.finders.interfaces.Finder;
import org.go.together.notification.comparators.impl.collection.transformers.interfaces.FinderTransformer;
import org.go.together.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.go.together.utils.ReflectionUtils.getClazzByType;

@Component
public class CommonFinderTransformer implements FinderTransformer {
    private Map<Class<?>, Finder> finderMap;

    @Autowired
    public void setClassComparatorMap(List<Finder> comparators) {
        this.finderMap = comparators.stream()
                .collect(Collectors.toMap(comparator -> ReflectionUtils.getParametrizedInterface(comparator.getClass(), 0),
                        Function.identity()));
    }

    @Override
    public Finder<?> get(ComparingObject fieldProperties) {
        Class<?> currentObjectType = getClazzByType(0, fieldProperties.getClazzType());
        return finderMap.entrySet().stream()
                .filter(entry -> entry.getKey() != Object.class)
                .filter(entry -> entry.getKey() == currentObjectType || entry.getKey().isAssignableFrom(currentObjectType))
                .map(Map.Entry::getValue)
                .findFirst().orElse(finderMap.get(Object.class));
    }
}
