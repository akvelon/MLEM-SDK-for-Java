package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to validation
 * of the method name property.
 */
public class IllegalMethodException extends RuntimeException {

    public IllegalMethodException(String message) {
        super(message);
    }
}
