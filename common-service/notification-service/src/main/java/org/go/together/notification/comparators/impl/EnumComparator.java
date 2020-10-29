package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.interfaces.NamedEnum;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

@Component
public class EnumComparator implements Comparator<NamedEnum> {
    @Override
    public String compare(String fieldName, NamedEnum originalObject, NamedEnum changedObject, boolean idCompare) {
        if (originalObject != changedObject) {
            return fieldName + FROM + originalObject.getDescription() + TO + changedObject.getDescription();
        }
        return StringUtils.EMPTY;
    }
}
