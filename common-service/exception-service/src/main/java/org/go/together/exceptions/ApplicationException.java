package org.go.together.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApplicationException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(ApplicationException.class);

    public ApplicationException(Exception exception) {
        super("Server error: " + exception.getMessage());
        log.error("Server error: " + exception.getMessage());

    }
}
