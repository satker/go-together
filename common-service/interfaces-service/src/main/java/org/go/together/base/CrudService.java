package org.go.together.base;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;

import java.util.UUID;

public interface CrudService<D extends Dto> {
    default IdDto create(D dto) {
        return create(UUID.randomUUID(), dto);
    }

    IdDto create(UUID requestId, D dto);

    default IdDto update(D dto) {
        return update(UUID.randomUUID(), dto);
    }

    IdDto update(UUID requestId, D dto);

    default D read(UUID uuid) {
        return read(UUID.randomUUID(), uuid);
    }

    D read(UUID requestId, UUID uuid);

    default void delete(UUID uuid) {
        delete(UUID.randomUUID(), uuid);
    }

    void delete(UUID requestId, UUID uuid);
}
