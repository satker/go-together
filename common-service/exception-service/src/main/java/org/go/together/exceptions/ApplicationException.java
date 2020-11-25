package org.go.together.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApplicationException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(ApplicationException.class);

    public ApplicationException(Exception exception, UUID requestId) {
        super("Server error: " + exception.getMessage() + ". Request id " + requestId);
        log.error("Server error: " + exception.getMessage() + ". Request id " + requestId);
    }

    public ApplicationException(String message, UUID requestId) {
        super("Server error: " + message + ". Request id " + requestId);
        log.error("Server error: " + message + ". Request id " + requestId);
    }
}
