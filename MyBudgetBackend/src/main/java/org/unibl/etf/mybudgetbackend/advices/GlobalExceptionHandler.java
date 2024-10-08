package org.unibl.etf.mybudgetbackend.advices;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.unibl.etf.mybudgetbackend.exceptions.HttpException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * This class centralizes the handling of exceptions that occur
 * across different parts of the application.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final HttpServletRequest request;

    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Handles exceptions that occur when the HTTP request body is not parsable.
     * Returns a 400 Bad Request status.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HandlerMethod handlerMethod) {
        log.warn("Client [" + request.getRemoteAddr() + "] invalid request body.");
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles an exception that occurs when validation of input data on the server side has not passed.
     * Returns a 400 Bad Request status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("Client [" + request.getRemoteAddr() + "] input data not valid " + exception.getBindingResult().getAllErrors().toString());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles custom-defined HttpException exceptions.
     * Returns the HTTP status and data specified in the exception,
     * or a 500 Internal Server Error if the status is not defined.
     */
    @ExceptionHandler(HttpException.class)
    public final ResponseEntity<Object> handleHttpException(HttpException e, HandlerMethod handlerMethod) {
        log.warn("Client [" + request.getRemoteAddr() + "] Http exception:", e);
        if (e.getStatus() == null) return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(e.getData(), e.getStatus());
    }

    /**
     * Handles all unhandled exceptions.
     * Returns a 500 Internal Server Error status code.
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e) {
        StringBuilder builder = new StringBuilder();
        builder.append(e);
        builder.append(System.lineSeparator());
        for (StackTraceElement element : e.getStackTrace()) {
            builder.append(element);
            builder.append(System.lineSeparator());
        }
        log.error(builder.toString());
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
