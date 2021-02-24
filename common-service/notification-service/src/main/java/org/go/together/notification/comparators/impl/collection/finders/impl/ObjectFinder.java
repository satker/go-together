package org.go.together.notification.comparators.impl.collection.finders.impl;

import org.go.together.compare.FieldProperties;
import org.go.together.notification.comparators.impl.collection.finders.interfaces.Finder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ObjectFinder implements Finder<Object> {

    @Override
    public Map<String, Object> findChanged(Collection<Object> changedObject,
                                           Collection<Object> originalObject,
                                           FieldProperties fieldProperties) {
        return new HashMap<>();
    }

    @Override
    public Collection<String> findAdded(Collection<Object> changedObject,
                                        Collection<Object> originalObject) {
        return changedObject.stream()
                .map(Optional::ofNullable)
                .filter(obj -> obj.isEmpty() || !originalObject.contains(obj))
                .map(obj -> obj.map(Object::toString).orElse(null))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<String> findRemoved(Collection<Object> changedObject,
                                          Collection<Object> originalObject) {
        return originalObject.stream()
                .filter(obj -> !changedObject.contains(obj))
                .map(Object::toString)
                .collect(Collectors.toSet());
    }
}
