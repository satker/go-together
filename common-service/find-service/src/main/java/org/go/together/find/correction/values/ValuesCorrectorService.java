package org.go.together.find.correction.values;

import org.go.together.find.correction.field.dto.CorrectedFieldDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ValuesCorrectorService implements ValuesCorrector {
    public Collection<Map<String, Object>> correct(CorrectedFieldDto correctedFieldDto,
                                                   Collection<Map<String, Object>> filters) {
        Collection<Map<String, Object>> result = new HashSet<>();
        Map<String, Class<?>> oldValueClass = correctedFieldDto.getOldValueClass();
        for (Map<String, Object> next : filters) {
            Map<String, Object> map = new HashMap<>();
            correctedFieldDto.getOldNewFilterField().forEach((oldKey, newKey) -> {
                Object value = next.get(oldKey);
                Class<?> parsedClass = oldValueClass.getOrDefault(oldKey, Object.class);
                Object parsedValue = parseFilterObjectToClass(value, parsedClass);
                map.put(newKey, parsedValue);
            });
            result.add(map);
        }
        return result;
    }

    private Object parseFilterObjectToClass(Object value, Class<?> clazz) {
        if (clazz == null) {
            return value;
        }
        if (value instanceof Collection) {
            return ((Collection<?>) value).stream()
                    .map(val -> parseFilterObjectToClass(val, clazz))
                    .collect(Collectors.toSet());
        } else if (clazz == String.class && value instanceof String) {
            return String.valueOf(value);
        } else if (clazz == UUID.class && value instanceof String) {
            return UUID.fromString(String.valueOf(value));
        } else if (clazz == Date.class && value instanceof Date) {
            return value;
        } else if (clazz == Number.class && value instanceof Number) {
            return value;
        } else if (Enum.class.isAssignableFrom(clazz) && value instanceof String) {
            return Enum.valueOf((Class) clazz, String.valueOf(value));
        } else {
            return value;
        }
    }
}
