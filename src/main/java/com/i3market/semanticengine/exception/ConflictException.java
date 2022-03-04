package com.i3market.semanticengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends ResponseStatusException {
    public ConflictException(HttpStatus status) {
        super(status);
    }

    public ConflictException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public ConflictException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public ConflictException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
