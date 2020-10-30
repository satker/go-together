package org.go.together.notification.comparators.impl;

import org.go.together.dto.ComparingObject;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class StringComparator implements Comparator<String> {
    @Override
    public Map<String, Object> compare(String fieldName, String originalObject, String changedObject, ComparingObject fieldProperties) {
        if (!originalObject.equalsIgnoreCase(changedObject)) {
            return Map.of(fieldName, originalObject + TO + changedObject);
        }
        return Collections.emptyMap();
    }
}
