package org.go.together.base;

import org.go.together.dto.Dto;
import org.go.together.enums.CrudOperation;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public interface Validator<D extends Dto> {
    default String validate(D dto, CrudOperation crudOperation) {
        return validate(UUID.randomUUID(), dto, crudOperation);
    }

    String validate(UUID requestId, D dto, CrudOperation crudOperation);

    default String validateDtos(Collection<D> dtos, CrudOperation crudOperation) {
        return validateDtos(UUID.randomUUID(), dtos, crudOperation);
    }

    String validateDtos(UUID requestId, Collection<D> dtos, CrudOperation crudOperation);

    default Map<String, Function<D, ?>> getMapsForCheck(UUID requestId) {
        return Collections.emptyMap();
    }
}
