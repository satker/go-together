package org.go.together.validation.validators.interfaces;

public interface ObjectValidator<T> {
    String checkDto(String fieldName, T result);
}
