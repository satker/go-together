package org.go.together.validation;

import org.go.together.enums.CrudOperation;
import org.go.together.interfaces.Dto;

import java.util.Collection;

public interface Validator<D extends Dto> {
    String validate(D dto, CrudOperation crudOperation);

    String validateDtos(Collection<D> dtos, CrudOperation crudOperation);

    void getMapsForCheck(D dto);
}
