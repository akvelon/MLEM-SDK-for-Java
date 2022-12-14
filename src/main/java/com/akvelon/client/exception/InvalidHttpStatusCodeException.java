package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to http status code.
 */
public class InvalidHttpStatusCodeException extends RuntimeException {
    /**
     * HTTP status code.
     */
    private final int statusCode;
    /**
     * HTTP exception message.
     */
    private final String message;

    /**
     * The constructor for creation a new instance of RestException.
     *
     * @param message    is the exception message.
     * @param statusCode is the status code.
     */
    public InvalidHttpStatusCodeException(String message, int statusCode) {
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

    @Override
    public String toString() {
        return "RestException{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}