package org.go.together.interfaces;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.go.together.utils.ReflectionUtils.getClazzByType;

public interface ImplFinder<T> {
    void setImpl(List<T> comparators);

    Map<Class<?>, T> getImpls();

    default T get(Type clazzType) {
        Class<?> currentObjectType = getClazzByType(0, clazzType);
        return getImpls().entrySet().stream()
                .filter(entry -> entry.getKey() != Object.class)
                .filter(entry -> entry.getKey() == currentObjectType || entry.getKey().isAssignableFrom(currentObjectType))
                .map(Map.Entry::getValue)
                .findFirst().orElse(getImpls().get(Object.class));
    }
}
