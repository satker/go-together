package org.go.together.notification.comparators.impl;

import org.go.together.compare.FieldProperties;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class DefaultComparator implements Comparator<Object> {

    @Override
    public Map<String, Object> compare(String fieldName, Object originalObject, Object changedObject, FieldProperties fieldProperties) {
        if (originalObject != changedObject) {
            return Map.of(fieldName, CHANGED);
        }
        return Collections.emptyMap();
    }
}
