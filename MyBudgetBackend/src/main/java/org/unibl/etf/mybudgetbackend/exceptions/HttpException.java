package org.unibl.etf.mybudgetbackend.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * Custom exception class to handle HTTP-related exceptions in the application.
 * This class extends RuntimeException and includes an HTTP status code
 * and optional data related to the exception.
 */
@Getter
@Setter
@ToString
public class HttpException extends RuntimeException {


    //Http status code on the request in which the exception occurred
    private HttpStatus status;

    //Data that describes the exception in more detail
    private Object data;

    public HttpException() {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.data = null;
    }

    public HttpException(Object data) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, data);
    }

    public HttpException(HttpStatus status) {
        this(status, null);
    }

    public HttpException(HttpStatus status, Object data) {
        this.status = status;
        this.data = data;
    }

}