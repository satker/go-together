package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

@Component
public class ObjectComparator implements Comparator<Object> {
    @Override
    public String compare(String fieldName, Object originalObject, Object changedObject, boolean idCompare) {
        if (originalObject != changedObject) {
            return fieldName + StringUtils.SPACE + CHANGED;
        }
        return StringUtils.EMPTY;
    }
}
