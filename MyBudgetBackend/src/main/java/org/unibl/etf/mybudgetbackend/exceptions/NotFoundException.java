package org.unibl.etf.mybudgetbackend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class representing a Not Found (404) HTTP status.
 * This exception should be thrown when a requested resource cannot be found.
 */
public class NotFoundException extends HttpException {
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, "Resource not found.");
    }

    public NotFoundException(Object data) {
        super(HttpStatus.NOT_FOUND, data);
    }
}
