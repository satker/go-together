package org.go.together.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class IncorrectFindObject extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(IncorrectFindObject.class);

    public IncorrectFindObject(String message) {
        super(message);
        log.error(message);
    }
}
