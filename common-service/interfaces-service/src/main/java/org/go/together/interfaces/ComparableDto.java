package org.go.together.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public interface ComparableDto {
    @JsonIgnore
    UUID getOwnerId();

    @JsonIgnore
    UUID getParentId();
}
