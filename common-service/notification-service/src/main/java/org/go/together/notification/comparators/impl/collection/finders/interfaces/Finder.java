package org.go.together.notification.comparators.impl.collection.finders.interfaces;

import org.go.together.compare.ComparingObject;

import java.util.Collection;
import java.util.Map;

public interface Finder<T> {
    Map<String, Object> findChanged(Collection<T> changedObject,
                                    Collection<T> originalObject,
                                    ComparingObject fieldProperties);

    Collection<String> findAdded(Collection<T> changedObject,
                                 Collection<T> originalObject);

    Collection<String> findRemoved(Collection<T> changedObject,
                                   Collection<T> originalObject);
}
