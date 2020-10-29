package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

@Component
public class StringComparator implements Comparator<String> {
    public String compare(String fieldName, String originalObject, String changedObject, boolean idCompare) {
        if (!originalObject.equalsIgnoreCase(changedObject)) {
            return fieldName + FROM + originalObject + TO + changedObject;
        }
        return StringUtils.EMPTY;
    }
}
