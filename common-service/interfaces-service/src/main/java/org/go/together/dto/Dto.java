package org.go.together.dto;

import lombok.EqualsAndHashCode;
import org.go.together.interfaces.Identified;

import java.util.UUID;

@EqualsAndHashCode
public class Dto implements Identified {
    private UUID id;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }
}
