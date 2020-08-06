package org.go.together.repository.exceptions;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApplicationException extends RuntimeException {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ApplicationException.class);

    public ApplicationException(Exception exception) {
        super("Server error: " + exception.getMessage());
        log.error("Server error: " + exception.getMessage());

    }
}