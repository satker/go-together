package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

@Component
public class NumberComparator implements Comparator<Number> {
    @Override
    public String compare(String fieldName, Number originalObject, Number changedObject, boolean idCompare) {
        if (!originalObject.equals(changedObject)) {
            return fieldName + FROM + originalObject.toString() + TO + changedObject.toString();
        }
        return StringUtils.EMPTY;
    }
}
