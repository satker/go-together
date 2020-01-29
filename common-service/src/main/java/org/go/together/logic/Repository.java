package org.go.together.logic;

import org.jooq.impl.UpdatableRecordImpl;

import java.util.UUID;

public interface Repository<E extends UpdatableRecordImpl> {
    E create(E entity);

    void delete(UUID uuid);

    E findById(UUID uuid);
}
