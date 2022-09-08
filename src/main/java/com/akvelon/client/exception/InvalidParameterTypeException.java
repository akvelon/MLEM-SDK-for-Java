package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to validation
 * of the Parameter type property.
 */
public class InvalidParameterTypeException extends RuntimeException {

    public InvalidParameterTypeException(String message) {
        super(message);
    }
}
