package org.go.together.logic.repository.utils.sql;

import java.util.Collection;
import java.util.stream.Collectors;

public class ObjectStringParser {
    public static String parseToString(Object val) {
        if (val instanceof String) {
            return "'" + val + "'";
        }
        if (val instanceof Number) {
            return Double.toString((Double) val);
        }
        if (val instanceof Collection) {
            return ((Collection) val).stream()
                    .collect(Collectors.joining(",", "(", ")")).toString();
        }
        return null;
    }
}
