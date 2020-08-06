package org.go.together.repository.exceptions;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidationException extends RuntimeException {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ValidationException.class);

    public ValidationException(String validationMessage) {
        super("Errors throw validation: " + validationMessage + ".");
        log.error("Errors throw validation: '" + validationMessage + "'.");
    }
}
