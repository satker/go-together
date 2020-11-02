package org.go.together.notification.comparators.transformers.interfaces;

import org.go.together.dto.ComparingObject;

public interface Transformer<T> {
    T get(String fieldName, Object originalObject, Object changedObject, ComparingObject fieldProperties);
}
