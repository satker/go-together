package org.go.together.notification.comparators.impl;

import org.go.together.compare.FieldProperties;
import org.go.together.interfaces.NamedEnum;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class EnumComparator implements Comparator<NamedEnum> {

    @Override
    public Map<String, Object> compare(String fieldName, NamedEnum originalObject, NamedEnum changedObject, FieldProperties fieldProperties) {
        if (originalObject != changedObject) {
            return Map.of(fieldName, Optional.ofNullable(originalObject).map(NamedEnum::getDescription).orElse(null) +
                    TO + Optional.ofNullable(changedObject).map(NamedEnum::getDescription).orElse(null));
        }
        return Collections.emptyMap();
    }
}
