package org.go.together.notification.comparators.interfaces;

import org.go.together.dto.ComparingObject;

import java.util.Map;

public interface Transformer {
    Map<String, Object> transform(String fieldName, Object originalObject, Object changedObject, ComparingObject fieldProperties);
}
