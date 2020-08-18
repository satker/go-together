package org.go.together.find.correction;
import java.util.*;

public class ValuesCorrector {
    public Collection<Map<String, Object>> getCorrectedValues(FieldCorrector fieldCorrector,
                                                               Collection<Map<String, Object>> filters) {
        Collection<Map<String, Object>> result = new HashSet<>();
        Map<String, Class> oldValueClass = fieldCorrector.getOldValueClass();
        for (Map<String, Object> next : filters) {
            Map<String, Object> map = new HashMap<>();
            fieldCorrector.getOldNewFilterField().forEach((oldKey, newKey) -> {
                Object value = next.get(oldKey);
                Class parsedClass = oldValueClass.getOrDefault(oldKey, Object.class);
                Object parsedValue = parseFilterObjectToClass(value, parsedClass);
                map.put(newKey, parsedValue);
            });
            result.add(map);
        }
        return result;
    }

    private Object parseFilterObjectToClass(Object value, Class clazz) {
        if (clazz == String.class && value instanceof String) {
            return String.valueOf(value);
        } else if (clazz == UUID.class && value instanceof String) {
            return UUID.fromString(String.valueOf(value));
        } else if (clazz == Date.class && value instanceof Date) {
            return value;
        } else if (clazz == Number.class && value instanceof Number) {
            return value;
        } else if (Enum.class.isAssignableFrom(clazz) && value instanceof String) {
            return Enum.valueOf(clazz, String.valueOf(value));
        } else {
            return value;
        }
    }
}
