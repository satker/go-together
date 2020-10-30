package org.go.together.notification.comparators.impl;

import org.go.together.dto.ComparingObject;
import org.go.together.interfaces.NamedEnum;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class EnumComparator implements Comparator<NamedEnum> {

    @Override
    public Map<String, Object> compare(String fieldName, NamedEnum originalObject, NamedEnum changedObject, ComparingObject fieldProperties) {
        if (originalObject != changedObject) {
            return Map.of(fieldName, originalObject.getDescription() + TO + changedObject.getDescription());
        }
        return Collections.emptyMap();
    }
}
