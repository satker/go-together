package org.go.together.interfaces;

import org.go.together.dto.ComparingObject;
import org.go.together.exceptions.IncorrectDtoException;

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
                            ComparingObject comparingObject =
                                    new ComparingObject(fieldValue, annotation.deepCompare(), annotation.isMain());
                            result.put(annotation.value(), comparingObject);
                        } catch (Exception e) {
                            throw new IncorrectDtoException("Cannot find field '" + name + "' getter");
                        }
                    }
                });
        return result;
    }

    default UUID getOwnerId() {
        return null;
    }
}
