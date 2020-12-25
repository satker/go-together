package org.go.together.validation.validators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.validation.dto.StringRegexDto;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RegexValidator implements ObjectValidator<StringRegexDto> {
    public static final String MESSAGE = "String %s is incorrect. ";

    @Override
    public String checkDto(String fieldName, StringRegexDto result) {
        return !Pattern.compile(result.getRegex())
                .matcher(result.getString()).matches() ?
                String.format(MESSAGE, fieldName) :
                StringUtils.EMPTY;
    }
}
