package org.go.together.validation.validators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommonObjectValidator implements ObjectValidator<Object> {
    public static final String MESSAGE = "Field %s is null. ";

    @Override
    public String checkDto(String fieldName, Object result) {
        return Objects.isNull(result) ?
                String.format(MESSAGE, fieldName) :
                StringUtils.EMPTY;
    }
}
