package com.i3market.semanticengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {

    public NotFoundException(final HttpStatus status) {
        super(status);
    }

    public NotFoundException(final HttpStatus status, final String reason) {
        super(status, reason);
    }

    public NotFoundException(final HttpStatus status, final String reason, final Throwable cause) {
        super(status, reason, cause);
    }

    public NotFoundException(final int rawStatusCode, final String reason, final Throwable cause) {
        super(rawStatusCode, reason, cause);
    }


}
