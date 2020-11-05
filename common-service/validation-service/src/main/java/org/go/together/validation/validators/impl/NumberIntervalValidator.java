package org.go.together.validation.validators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.validation.dto.NumberIntervalDto;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.stereotype.Component;

@Component
public class NumberIntervalValidator implements ObjectValidator<NumberIntervalDto> {
    public static final String MESSAGE = "Number interval for %s is incorrect. ";

    @Override
    public String checkDto(String fieldName, NumberIntervalDto result) {
        return result.getNumber().doubleValue() < result.getMin().doubleValue() ||
                result.getNumber().doubleValue() > result.getMax().doubleValue() ?
                String.format(MESSAGE, fieldName) :
                StringUtils.EMPTY;
    }
}
