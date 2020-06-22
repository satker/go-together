package org.go.together.interfaces;

import org.go.together.dto.ComparingObject;

import java.util.Map;
import java.util.UUID;

public interface Comparable {
    Map<String, ComparingObject> getComparingMap();

    default UUID getAuthorId() {
        return null;
    }
}
