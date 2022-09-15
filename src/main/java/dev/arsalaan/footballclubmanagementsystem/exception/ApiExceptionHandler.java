package dev.arsalaan.footballclubmanagementsystem.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/*
This class handles custom (or existing) exceptions.
It dictates how the exceptions are presented to the REST API client.
Spring Boot handles exceptions by default but this class allows custom handling
to fit our own requirements.

@ControllerAdvice - It allows you to handle exceptions across the whole application,
not just to an individual controller.
 */

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /* Provides handling for exceptions throughout this service.
    * Created to encapsulate errors with more detail and essentially replace javax.persistence.EntityNotFoundException
    * ApiRequestException ~= EntityNotFoundException */
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException ex) {

        // 1. Create payload containing exception details
        ApiException apiException = new ApiException(
                ex.getMessage(),
                //ex,
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        // 2. Return response entity
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    /* Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation. */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        ApiException apiException = new ApiException(
                "VALIDATION ERROR: " + ex.getBindingResult().getFieldErrors(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    // other exception handlers can be added here
    // (e.g. handleEntityNotFoundException, handleHttpMessageNotReadable,
    // handleUserNotFoundException, handleContentNotAllowedException, handleMethodArgumentNotValid)

}
