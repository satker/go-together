package org.go.together.exceptions;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ServiceTransportException extends RuntimeException {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(IncorrectDtoException.class);

    public ServiceTransportException(String message) {
        super(message);
        log.error(message);

    }
}
