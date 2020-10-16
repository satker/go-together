package org.go.together.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RemoteClientFindException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(RemoteClientFindException.class);

    public RemoteClientFindException(Exception e) {
        super("Cannot find by reason: " + e.getMessage());
        log.error("Cannot find by reason: " + e.getMessage());
    }
}
