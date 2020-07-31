package org.go.together.sql;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

public class ObjectStringParser {
    public static String parseToString(Object val) {
        if (val instanceof Date) {
            Date date = (Date) val;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSXXX");
            String formattedDate = formatter.format(date);
            return "'" + formattedDate + "'";
        }
        if (val instanceof String) {
            return "'" + val + "'";
        }
        if (val instanceof Double) {
            return Double.toString((Double) val);
        }
        if (val instanceof Boolean) {
            return ((Boolean) val).toString();
        }
        if (val instanceof Integer) {
            return Integer.toString((Integer) val);
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
                    .collect(Collectors.joining(",", "(", ")"));
        }
        return "'" + val.toString() + "'";
    }
}
