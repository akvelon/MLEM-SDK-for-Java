package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to validation
 * of the method name property.
 */
public class IllegalPathException extends RuntimeException {

    public IllegalPathException(String message) {
        super(message);
    }
}
