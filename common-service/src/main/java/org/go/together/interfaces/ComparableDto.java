package org.go.together.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.go.together.dto.ComparingObject;
import org.go.together.exceptions.IncorrectDtoException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public interface ComparableDto extends Identified {
    @JsonIgnore
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
                            boolean ignored = field.isAnnotationPresent(JsonIgnore.class);
                            ComparingObject comparingObject =
                                    new ComparingObject(fieldValue, annotation.deepCompare(), annotation.isMain(), ignored,
                                            annotation.idCompare());
                            result.put(annotation.value(), comparingObject);
                        } catch (Exception e) {
                            throw new IncorrectDtoException("Cannot find field '" + name + "' getter");
                        }
                    }
                });
        return result;
    }

    @JsonIgnore
    default UUID getOwnerId() {
        return null;
    }

    default UUID getParentId() {
        return getId();
    }
}
