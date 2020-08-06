package org.go.together.repository.exceptions;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotFindEntityException extends RuntimeException {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CannotFindEntityException.class);

    public CannotFindEntityException(String message) {
        super(message);
        log.error(message);

    }
}
