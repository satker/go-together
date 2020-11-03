package org.go.together.notification.comparators.interfaces;

import org.go.together.dto.ComparingObject;

import java.util.Map;

public interface Comparator<T> {
    String REMOVED = "-1";
    String ADDED = "1";
    String CHANGED = "0";
    String TO = " -> ";

    Map<String, Object> compare(String fieldName, T originalObject, T changedObject, ComparingObject fieldProperties);

    default Map<String, Object> compare(String fieldName, T originalObject, T changedObject) {
        return compare(fieldName, originalObject, changedObject, null);
    }
}
