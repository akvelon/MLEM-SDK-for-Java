package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to validation
 * of the number property.
 */
public class InvalidTypeException extends RuntimeException {

    public InvalidTypeException(String message) {
        super(message);
    }
}
