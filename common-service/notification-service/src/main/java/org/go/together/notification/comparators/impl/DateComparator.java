package org.go.together.notification.comparators.impl;

import org.go.together.compare.ComparingObject;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Component
public class DateComparator implements Comparator<Date> {

    @Override
    public Map<String, Object> compare(String fieldName, Date originalObject, Date changedObject, ComparingObject fieldProperties) {
        if (originalObject.compareTo(changedObject) != 0) {
            return Map.of(fieldName, originalObject.toString() + TO + changedObject.toString());
        }
        return Collections.emptyMap();
    }
}
