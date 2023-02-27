package com.i3market.semanticengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidInputException extends ResponseStatusException {

    public InvalidInputException(final HttpStatus status) {
        super(status);
    }

    public InvalidInputException(final HttpStatus status, final String reason) {
        super(status, reason);
    }

    public InvalidInputException(final HttpStatus status, final String reason, final Throwable cause) {
        super(status, reason, cause);
    }

    public InvalidInputException(final int rawStatusCode, final String reason, final Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

//    @Override
//    public HttpStatus getStatus() {
//        return super.getStatus();
//    }
}