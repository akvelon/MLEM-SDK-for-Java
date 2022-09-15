package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to validation
 * of the Parameter type property.
 */
public class InvalidParameterNameException extends RuntimeException {

    public InvalidParameterNameException(String message) {
        super(message);
    }
}
