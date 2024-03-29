package org.go.together.base;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;

import java.util.UUID;

public interface CrudService<D extends Dto> {
    IdDto create(D dto);

    IdDto update(D dto);

    D read(UUID uuid);

    void delete(UUID uuid);
}
