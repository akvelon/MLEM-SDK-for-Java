package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to validation
 * of the number property.
 */
public class IllegalNumberFormatException extends RuntimeException {

    public IllegalNumberFormatException(String message) {
        super(message);
    }
}
