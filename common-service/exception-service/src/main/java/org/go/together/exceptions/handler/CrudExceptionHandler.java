package org.go.together.exceptions.handler;

import org.go.together.exceptions.*;
import org.go.together.exceptions.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CrudExceptionHandler {
    @ExceptionHandler({
            ValidationException.class,
            IncorrectDtoException.class,
            IncorrectFindObject.class,
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto processValidationError(RuntimeException exception) {
        return ExceptionDto.builder().exceptionMessage(exception.getMessage()).build();
    }

    @ExceptionHandler({
            ApplicationException.class,
            CannotFindEntityException.class,
            RemoteClientFindException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto processApplicationError(RuntimeException exception) {
        return ExceptionDto.builder().exceptionMessage("Server error: contact to system administrator.").build();
    }
}
