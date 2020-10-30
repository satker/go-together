package org.go.together.notification.comparators.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.ComparingObject;

import java.util.Map;

public interface Comparator<T> {
    String REMOVED = "removed";
    String ADDED = "added";
    String CHANGED = "changed";
    String CHANGED_WITH_UPPER_LETTER = "Changed";
    String FROM = StringUtils.SPACE + CHANGED + ": ";
    String TO = " -> ";
    String COLON = ": ";
    String COMMA = ", ";
    String ITEMS = " items";
    String ITEM = "item";

    ObjectMapper objectMapper = new ObjectMapper();

    Map<String, Object> compare(String fieldName, T originalObject, T changedObject, ComparingObject fieldProperties);

    default Map<String, Object> compare(String fieldName, T originalObject, T changedObject) {
        return compare(fieldName, originalObject, changedObject, null);
    }
}
