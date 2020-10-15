package org.go.together.interfaces;

import java.util.UUID;

public interface Identified {
    UUID getId();

    void setId(UUID id);

    static <E extends Identified> E cast(Object obj) {
        if (obj instanceof Identified) {
            return (E) obj;
        }
        throw new IllegalArgumentException("Cannot cast object to  entity");
    }
}
