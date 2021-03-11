package org.go.together.exceptions.handler;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.go.together.exceptions.ApplicationException;
import org.go.together.exceptions.IncorrectDtoException;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.exceptions.ValidationException;
import org.go.together.exceptions.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class CrudExceptionHandler {
    private final Tracer tracer;

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
            ApplicationException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto processApplicationError() {
        return ExceptionDto.builder()
                .exceptionMessage("Server error: contact to system administrator. Trace error id: " +
                        tracer.currentSpan().context().traceIdString())
                .build();
    }
}
