package org.go.together.notification.comparators.transformers.interfaces;

import org.go.together.compare.FieldProperties;
import org.go.together.interfaces.ImplFinder;
import org.go.together.notification.comparators.interfaces.Comparator;

public interface Transformer<T> extends ImplFinder<Comparator> {
    T get(String fieldName, Object originalObject, Object changedObject, FieldProperties fieldProperties);
}
