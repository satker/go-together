package org.go.together.exceptions;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RemoteClientFindException extends RuntimeException {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RemoteClientFindException.class);

    public RemoteClientFindException(Exception e) {
        super("Cannot find by reason: " + e.getMessage());
        log.error("Cannot find by reason: " + e.getMessage());
    }
}
