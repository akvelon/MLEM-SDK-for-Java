package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to validation
 * of the parameter columns number.
 */
public class IllegalColumnsNumberException extends RuntimeException {

    public IllegalColumnsNumberException(String message) {
        super(message);
    }
}
