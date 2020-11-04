package org.go.together.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
public abstract class ComparableDto extends Dto {
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
}
