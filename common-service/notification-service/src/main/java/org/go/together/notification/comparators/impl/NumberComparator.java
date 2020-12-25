package org.go.together.notification.comparators.impl;

import org.go.together.compare.ComparingObject;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class NumberComparator implements Comparator<Number> {

    @Override
    public Map<String, Object> compare(String fieldName, Number originalObject, Number changedObject, ComparingObject fieldProperties) {
        if (!originalObject.equals(changedObject)) {
            return Map.of(fieldName, originalObject.toString() + TO + changedObject.toString());
        }
        return Collections.emptyMap();
    }
}
