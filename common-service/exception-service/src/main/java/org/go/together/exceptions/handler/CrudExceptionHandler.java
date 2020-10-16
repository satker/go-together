package org.go.together.exceptions.handler;

import org.go.together.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CrudExceptionHandler {
    @ExceptionHandler({
            ValidationException.class,
            ApplicationException.class,
            CannotFindEntityException.class,
            IncorrectDtoException.class,
            IncorrectFindObject.class,
            RemoteClientFindException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object processValidationError(RuntimeException exception) {
        return exception;
    }
}
