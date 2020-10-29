package org.go.together.notification.comparators.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.dto.SimpleDto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleDtoComparator implements Comparator<SimpleDto> {
    public static final String NAME = " name";
    private final Comparator<String> stringComparator;

    @Override
    public String compare(String fieldName, SimpleDto originalObject, SimpleDto changedObject, boolean idCompare) {
        return stringComparator.compare(fieldName + NAME, originalObject.getName(), changedObject.getName(), idCompare);
    }
}
