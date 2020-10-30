package org.go.together.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.go.together.dto.ComparingObject;
import org.go.together.utils.NotificationUtils;

import java.util.Map;
import java.util.UUID;

public abstract class ComparableDto implements Dto {
    private final Map<String, ComparingObject> comparingObject;

    protected ComparableDto() {
        this.comparingObject = NotificationUtils.getComparingMap(this.getClass());
    }

    @JsonIgnore
    public UUID getOwnerId() {
        return null;
    }

    @JsonIgnore
    public UUID getParentId() {
        return null;
    }

    @JsonIgnore
    public abstract String getMainField();

    @JsonIgnore
    public Map<String, ComparingObject> getComparingMap() {
        return comparingObject;
    }
}
