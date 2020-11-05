package org.go.together.validation.validators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.stereotype.Component;

@Component
public class NumberValidator implements ObjectValidator<Number> {
    public static final String MESSAGE = "Number %s is zero or negative ";

    @Override
    public String checkDto(String fieldName, Number result) {
        return result.doubleValue() <= 0 ?
                String.format(MESSAGE, fieldName) :
                StringUtils.EMPTY;
    }
}
