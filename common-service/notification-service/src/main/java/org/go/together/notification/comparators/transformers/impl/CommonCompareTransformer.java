package org.go.together.notification.comparators.transformers.impl;

import org.go.together.compare.ComparingObject;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.comparators.transformers.interfaces.Transformer;
import org.go.together.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.go.together.utils.ReflectionUtils.getClazz;

@Component
public class CommonCompareTransformer implements Transformer<Comparator> {
    private Map<Class<?>, Comparator> classComparatorMap;

    @Override
    @Autowired
    public void setImpl(List<Comparator> comparators) {
        this.classComparatorMap = comparators.stream()
                .collect(Collectors.toMap(comparator -> ReflectionUtils.getParametrizedInterface(comparator.getClass(), 0),
                        Function.identity()));
    }

    @Override
    public Map<Class<?>, Comparator> getImpls() {
        return classComparatorMap;
    }

    @Override
    public Comparator get(String fieldName, Object originalObject, Object changedObject, ComparingObject fieldProperties) {
        if (!fieldProperties.getIsDeepCompare() || fieldProperties.getIgnored() || fieldProperties.getIdCompare()) {
            return get(Object.class);
        }
        Class<?> clazz = getClazz(fieldProperties.getClazzType());

        return get(clazz);
    }
}
