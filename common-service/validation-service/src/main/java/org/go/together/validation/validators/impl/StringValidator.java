package org.go.together.validation.validators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.stereotype.Component;

@Component
public class StringValidator implements ObjectValidator<String> {
    public static final String MESSAGE = "Field %s is empty or null. ";

    @Override
    public String checkDto(String fieldName, String result) {
        return StringUtils.isBlank(result) ?
                String.format(MESSAGE, fieldName) :
                StringUtils.EMPTY;
    }
}
