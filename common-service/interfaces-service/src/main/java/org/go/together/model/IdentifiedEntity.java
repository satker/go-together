package org.go.together.model;

import lombok.EqualsAndHashCode;
import org.go.together.interfaces.Identified;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@EqualsAndHashCode
public abstract class IdentifiedEntity implements Identified {
    @Id
    @Column(columnDefinition = "uuid")
    protected UUID id;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
