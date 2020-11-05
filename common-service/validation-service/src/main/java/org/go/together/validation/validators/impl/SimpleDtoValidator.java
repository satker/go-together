package org.go.together.validation.validators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.SimpleDto;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.stereotype.Component;

@Component
public class SimpleDtoValidator implements ObjectValidator<SimpleDto> {
    public static final String MESSAGE = "Field %s is incorrect. ";

    private static boolean isCorrectSimpleDto(SimpleDto simpleDto) {
        return simpleDto == null || simpleDto.getId() == null || StringUtils.isBlank(simpleDto.getName());
    }

    @Override
    public String checkDto(String fieldName, SimpleDto result) {
        return result == null || isCorrectSimpleDto(result) ?
                String.format(MESSAGE, fieldName) :
                StringUtils.EMPTY;
    }
}
