package org.go.together.notification.comparators.interfaces;

import org.apache.commons.lang3.StringUtils;

public interface Comparator<T> {
    String CHANGED = "changed";
    String CHANGED_WITH_UPPER_LETTER = "Changed";
    String FROM = StringUtils.SPACE + CHANGED + ": ";
    String TO = " -> ";
    String COLON = ": ";
    String COMMA = ", ";

    String compare(String fieldName, T originalObject, T changedObject, boolean idCompare);
}
