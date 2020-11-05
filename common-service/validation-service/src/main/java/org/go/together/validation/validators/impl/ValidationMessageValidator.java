package org.go.together.validation.validators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.stereotype.Component;

@Component
public class ValidationMessageValidator implements ObjectValidator<ValidationMessageDto> {
    public static final String MESSAGE = "Field %s not passed validation check: %s .";

    @Override
    public String checkDto(String fieldName, ValidationMessageDto result) {
        if (StringUtils.isNotBlank(result.getMessage())) {
            return String.format(MESSAGE, fieldName, result.getMessage());
        }
        return StringUtils.EMPTY;
    }
}
