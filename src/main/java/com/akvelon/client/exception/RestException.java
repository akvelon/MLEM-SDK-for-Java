package com.akvelon.client.exception;

/**
 * The class represent the custom exception to store the status code and message
 */
public class RestException extends RuntimeException {
    /**
     * HTTP status code
     */
    private final int statusCode;
    /**
     * HTTP exception message
     */
    private final String message;

    /**
     * The constructor for creation a new instance of RestException
     *
     * @param message    is the exception message
     * @param statusCode is the status code
     */
    public RestException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return this.message;
    }
}