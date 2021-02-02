package org.go.together.notification.comparators.impl;

import org.go.together.compare.ComparingObject;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Component
public class UUIDComparator implements Comparator<UUID> {
    @Override
    public Map<String, Object> compare(String fieldName, UUID originalUUID, UUID changedUUID, ComparingObject fieldProperties) {
        if (!originalUUID.equals(changedUUID)) {
            return Map.of(fieldName, CHANGED);
        }
        return Collections.emptyMap();
    }
}