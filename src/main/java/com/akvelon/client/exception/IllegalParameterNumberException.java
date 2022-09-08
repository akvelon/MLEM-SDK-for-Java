package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to validation
 * of the Parameter number.
 */
public class IllegalParameterNumberException extends RuntimeException {

    public IllegalParameterNumberException(String message) {
        super(message);
    }
}
