package org.go.together.notification.comparators.impl.collection.finders.impl;

import org.go.together.compare.ComparingObject;
import org.go.together.notification.comparators.impl.collection.finders.interfaces.Finder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ObjectFinder implements Finder<Object> {

    @Override
    public Map<String, Object> findChanged(Collection<Object> changedObject,
                                           Collection<Object> originalObject,
                                           ComparingObject fieldProperties) {
        return new HashMap<>();
    }

    @Override
    public Collection<String> findAdded(Collection<Object> changedObject,
                                        Collection<Object> originalObject) {
        return changedObject.stream()
                .filter(obj -> !originalObject.contains(obj))
                .map(Object::toString)
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
