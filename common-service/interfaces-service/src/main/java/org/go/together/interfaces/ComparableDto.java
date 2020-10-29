package org.go.together.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.go.together.dto.ComparingObject;
import org.go.together.utils.NotificationUtils;

import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

public abstract class ComparableDto implements Dto {
    private final Map<String, ComparingObject> comparingObject;
    private String mainField;

    protected ComparableDto() {
        this.comparingObject = NotificationUtils.getComparingMap(this.getClass());

    }

    @JsonIgnore
    public abstract UUID getOwnerId();

    @JsonIgnore
    public abstract UUID getParentId();

    @JsonIgnore
    public String getMainField() {
        if (nonNull(mainField)) {
            return mainField;
        } else {
            return NotificationUtils.getMainField(comparingObject, this);
        }
    }

    @JsonIgnore
    public Map<String, ComparingObject> getComparingMap() {
        return comparingObject;
    }
}
