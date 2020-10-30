package org.go.together.notification.comparators.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.dto.ComparingObject;
import org.go.together.dto.SimpleDto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SimpleDtoComparator implements Comparator<SimpleDto> {
    public static final String NAME = " name";
    private final Comparator<String> stringComparator;

    @Override
    public Map<String, Object> compare(String fieldName, SimpleDto originalObject, SimpleDto changedObject, ComparingObject fieldProperties) {
        return stringComparator.compare(fieldName + NAME, originalObject.getName(), changedObject.getName(), fieldProperties);
    }
}
