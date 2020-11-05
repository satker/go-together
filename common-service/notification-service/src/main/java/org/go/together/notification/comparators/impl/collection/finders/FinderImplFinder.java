package org.go.together.notification.comparators.impl.collection.finders;

import org.go.together.interfaces.ImplFinder;
import org.go.together.notification.comparators.impl.collection.finders.interfaces.Finder;
import org.go.together.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FinderImplFinder implements ImplFinder<Finder> {
    private Map<Class<?>, Finder> finderMap;

    @Autowired
    public void setImpl(List<Finder> comparators) {
        this.finderMap = comparators.stream()
                .collect(Collectors.toMap(comparator -> ReflectionUtils.getParametrizedInterface(comparator.getClass(), 0),
                        Function.identity()));
    }

    public Map<Class<?>, Finder> getImpls() {
        return finderMap;
    }
}
