package org.go.together.logic.repository.utils.sql;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class ObjectStringParser {
    public static String parseToString(Object val) {
        if (val instanceof String) {
            return "'" + val + "'";
        }
        if (val instanceof Number) {
            return Double.toString((Double) val);
        }
        if (val instanceof UUID) {
            return "'" + val.toString() + "'";
        }
        if (val instanceof Enum) {
            return String.valueOf(((Enum) val).ordinal());
        }
        if (val instanceof Collection) {
            return ((Collection<Object>) val).stream()
                    .map(ObjectStringParser::parseToString)
                    .collect(Collectors.joining(",", "(", ")")).toString();
        }
        return "'" + val.toString() + "'";
    }
}
