package org.go.together.base;

import org.go.together.dto.IdDto;
import org.go.together.find.FindService;
import org.go.together.interfaces.Dto;

import java.util.UUID;

public interface CrudService<D extends Dto> extends FindService {
    IdDto create(D dto);

    IdDto update(D dto);

    D read(UUID uuid);

    void delete(UUID uuid);
}
