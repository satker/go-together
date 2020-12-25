package org.go.together.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidationException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(ValidationException.class);

    public ValidationException(UUID requestId, String validationMessage, String serviceName) {
        super("Request id: " + requestId + ". Errors throw validation: " + validationMessage);
        log.error(serviceName + " validation failed: " + validationMessage);
        log.error("Request id: " + requestId + ". Errors throw validation: " + validationMessage);
    }
}
