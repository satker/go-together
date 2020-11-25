package org.go.together.base;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;

import java.util.UUID;

public interface CrudService<D extends Dto> {
    default IdDto create(D dto) {
        return create(null, dto);
    }

    IdDto create(UUID requestId, D dto);

    default IdDto update(D dto) {
        return update(null, dto);
    }

    IdDto update(UUID requestId, D dto);

    default D read(UUID uuid) {
        return read(null, uuid);
    }

    D read(UUID requestId, UUID uuid);

    default void delete(UUID uuid) {
        delete(null, uuid);
    }

    void delete(UUID requestId, UUID uuid);
}
