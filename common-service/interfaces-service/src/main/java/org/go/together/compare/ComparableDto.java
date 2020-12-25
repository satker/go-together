package org.go.together.compare;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import org.go.together.dto.Dto;

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
