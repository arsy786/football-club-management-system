package dev.arsalaan.footballclubmanagementsystem.exception;

/*
This class is the Custom exception that is thrown throughout the API.
E.g: throw new ApiRequestException (instead of standard IllegalStateException)
 */

public class ApiRequestException extends RuntimeException{

    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
