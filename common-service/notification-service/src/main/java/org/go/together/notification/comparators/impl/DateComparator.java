package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateComparator implements Comparator<Date> {
    @Override
    public String compare(String fieldName, Date originalObject, Date changedObject, boolean idCompare) {
        if (originalObject.compareTo(changedObject) != 0) {
            return fieldName + FROM + originalObject.toString() + TO + changedObject.toString();
        }
        return StringUtils.EMPTY;
    }
}
