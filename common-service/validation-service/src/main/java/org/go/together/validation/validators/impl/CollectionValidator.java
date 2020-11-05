package org.go.together.validation.validators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

@Component
public class CollectionValidator implements ObjectValidator<Collection<?>> {
    public static final String MESSAGE = "Collection %s is incorrect. ";

    @Override
    public String checkDto(String fieldName, Collection<?> result) {
        boolean b = result == null || result.isEmpty() ||
                result.stream().anyMatch(Objects::isNull);
        return b ? String.format(MESSAGE, fieldName) :
                StringUtils.EMPTY;
    }
}
