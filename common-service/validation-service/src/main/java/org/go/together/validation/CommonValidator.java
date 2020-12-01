package org.go.together.validation;

import org.apache.commons.lang3.StringUtils;
import org.go.together.base.Validator;
import org.go.together.dto.Dto;
import org.go.together.enums.CrudOperation;
import org.go.together.interfaces.ImplFinder;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.go.together.utils.ReflectionUtils.getClazz;

public abstract class CommonValidator<D extends Dto> implements Validator<D> {
    private ImplFinder<ObjectValidator> implFinder;

    @Autowired
    public void setImplFinder(ImplFinder<ObjectValidator> implFinder) {
        this.implFinder = implFinder;
    }

    @Override
    public String validate(UUID requestId, D dto, CrudOperation crudOperation) {
        if (requestId == null) {
            return "Request id is null";
        }
        if (dto == null) {
            return "Dto is null";
        }
        Set<String> errors = getMapsForCheck(requestId).entrySet().stream()
                .map(entry -> getValidatorResult(entry, dto))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        if (errors.isEmpty()) {
            return commonValidation(requestId, dto, crudOperation);
        }
        return String.join(StringUtils.EMPTY, errors);
    }

    private String getValidatorResult(Map.Entry<String, Function<D, ?>> entry, D dto) {
        Object checkedValue = entry.getValue().apply(dto);
        Class<?> checkedType = Objects.nonNull(checkedValue) ? getClazz(checkedValue.getClass()) : Object.class;
        return implFinder.get(checkedType).checkDto(entry.getKey(), checkedValue);
    }

    @Override
    public String validateDtos(UUID requestId, Collection<D> dtos, CrudOperation crudOperation) {
        return dtos.stream()
                .map(dto -> validate(requestId, dto, crudOperation))
                .collect(Collectors.joining());
    }

    protected String commonValidation(UUID requestId, D dto, CrudOperation crudOperation) {
        return StringUtils.EMPTY;
    }

}