package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to deserialization
 * of the response type.
 */
public class InvalidResponseTypeException extends RuntimeException {

    public InvalidResponseTypeException(String message) {
        super(message);
    }
}
