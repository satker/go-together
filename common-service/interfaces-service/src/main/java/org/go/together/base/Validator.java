package org.go.together.base;

import org.go.together.dto.Dto;
import org.go.together.enums.CrudOperation;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

public interface Validator<D extends Dto> {
    String validate(D dto, CrudOperation crudOperation);

    String validateDtos(Collection<D> dtos, CrudOperation crudOperation);

    default Map<String, Function<D, ?>> getMapsForCheck() {
        return Collections.emptyMap();
    }
}
