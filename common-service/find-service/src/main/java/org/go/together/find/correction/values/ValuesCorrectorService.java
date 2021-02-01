package org.go.together.find.correction.values;

import org.go.together.dto.FilterValueDto;
import org.go.together.find.correction.path.dto.Path;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ValuesCorrectorService implements ValuesCorrector {
    public FilterValueDto correct(Path correctedFieldDto,
                                  FilterValueDto values) {
        Class<?> parsedClass = correctedFieldDto.getLastFieldMapper().getFieldClass();
        Object parsedValue = parseFilterObjectToClass(values.getValue(), parsedClass);
        return new FilterValueDto(values.getFilterType(), parsedValue);
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
