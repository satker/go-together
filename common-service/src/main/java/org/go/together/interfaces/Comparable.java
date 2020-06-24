package org.go.together.interfaces;

import org.go.together.dto.ComparingObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public interface Comparable {
    default Map<String, ComparingObject> getComparingMap() {
        HashMap<String, ComparingObject> result = new HashMap<>();
        Stream.of(this.getClass().getDeclaredFields())
                .forEach(field -> {
                    ComparingField annotation = field.getAnnotation(ComparingField.class);
                    if (annotation != null) {
                        String name = field.getName();
                        String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                        try {
                            Method fieldGetter = this.getClass().getDeclaredMethod(methodName);
                            Object fieldValue = fieldGetter.invoke(this);
                            result.put(annotation.value(),
                                    ComparingObject.builder().fieldValue(fieldValue)
                                            .isMain(annotation.isMain())
                                            .isNeededDeepCompare(annotation.deepCompare())
                                            .build());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        return result;
    }

    default UUID getOwnerId() {
        return null;
    }
}
