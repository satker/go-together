package org.go.together.validation;

import org.apache.commons.lang3.StringUtils;
import org.go.together.async.validation.AsyncValidator;
import org.go.together.base.Validator;
import org.go.together.dto.Dto;
import org.go.together.enums.CrudOperation;
import org.go.together.interfaces.ImplFinder;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.go.together.utils.ReflectionUtils.getClazz;

public abstract class CommonValidator<D extends Dto> implements Validator<D> {
    private ImplFinder<ObjectValidator> implFinder;
    private AsyncValidator asyncValidator;

    @Autowired
    public void setAsyncValidator(AsyncValidator asyncValidator) {
        this.asyncValidator = asyncValidator;
    }

    @Autowired
    public void setImplFinder(ImplFinder<ObjectValidator> implFinder) {
        this.implFinder = implFinder;
    }

    @Override
    public String validate(D dto, CrudOperation crudOperation) {
        if (dto == null) {
            return "Dto is null";
        }
        Set<String> errors = getErrors(dto);
        if (errors.isEmpty()) {
            return commonValidation(dto, crudOperation);
        }
        return String.join(StringUtils.EMPTY, errors);
    }

    private Set<String> getErrors(D dto) {
        return asyncValidator.validate(this::wrapAsync, getMapsForCheck(), dto);
    }

    private Callable<String> wrapAsync(Map.Entry<String, Function<D, ?>> entry, D dto) {
        return () -> getValidatorResult(entry, dto);
    }

    private String getValidatorResult(Map.Entry<String, Function<D, ?>> entry, D dto) {
        Object checkedValue = entry.getValue().apply(dto);
        Class<?> checkedType = Objects.nonNull(checkedValue) ? getClazz(checkedValue.getClass()) : Object.class;
        return implFinder.get(checkedType).checkDto(entry.getKey(), checkedValue);
    }

    @Override
    public String validateDtos(Collection<D> dtos, CrudOperation crudOperation) {
        return dtos.stream()
                .map(dto -> validate(dto, crudOperation))
                .collect(Collectors.joining());
    }

    protected String commonValidation(D dto, CrudOperation crudOperation) {
        return StringUtils.EMPTY;
    }
}
