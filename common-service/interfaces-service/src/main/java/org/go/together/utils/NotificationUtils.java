package org.go.together.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.ComparingObject;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class NotificationUtils {
    public static <T extends ComparableDto> Map<String, ComparingObject> getComparingMap(Class<T> clazz) {
        HashMap<String, ComparingObject> result = new HashMap<>();
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
                            ComparingObject comparingObject =
                                    new ComparingObject(fieldValueGetter, annotation.deepCompare(), annotation.isMain(), ignored,
                                            annotation.idCompare());
                            result.put(annotation.value(), comparingObject);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Cannot find field '" + name + "' getter");
                        }
                    }
                });
        return result;
    }

    public static <T extends ComparableDto> String getMainField(Map<String, ComparingObject> comparingObject, T dto) {
        return comparingObject.values().stream()
                .filter(ComparingObject::getIsMain)
                .findFirst()
                .map(ComparingObject::getFieldValueGetter)
                .map(fieldGetter -> fieldGetter.apply(dto))
                .map(Object::toString)
                .map(string -> " '" + string + "'")
                .orElse(StringUtils.EMPTY);
    }
}
