package org.go.together.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.go.together.compare.ComparingField;
import org.go.together.compare.FieldProperties;
import org.go.together.dto.Dto;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class NotificationUtils {
    public static <T extends Dto> Map<String, FieldProperties> getComparingMap(Class<T> clazz) {
        HashMap<String, FieldProperties> result = new HashMap<>();
        Stream.of(clazz.getDeclaredFields())
                .forEach(field -> {
                    ComparingField annotation = field.getAnnotation(ComparingField.class);
                    if (annotation != null) {
                        String name = field.getName();
                        String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                        try {
                            Method fieldGetter = clazz.getDeclaredMethod(methodName);
                            Function<T, Object> fieldValueGetter = dto -> {
                                try {
                                    return fieldGetter.invoke(dto);
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("Cannot get value by field " + name + ": " + e);
                                }
                            };
                            boolean ignored = field.isAnnotationPresent(JsonIgnore.class);
                            FieldProperties fieldProperties = FieldProperties.builder()
                                    .fieldValueGetter(fieldValueGetter)
                                    .isDeepCompare(annotation.deepCompare())
                                    .idCompare(annotation.idCompare())
                                    .clazzType(field.getGenericType())
                                    .ignored(ignored).build();
                            result.put(annotation.value(), fieldProperties);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Cannot find field '" + name + "' getter");
                        }
                    }
                });
        return result;
    }
}
