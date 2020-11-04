package org.go.together.repository.entities;

import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class NamedIdentifiedEntity extends IdentifiedEntity {
    protected String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
