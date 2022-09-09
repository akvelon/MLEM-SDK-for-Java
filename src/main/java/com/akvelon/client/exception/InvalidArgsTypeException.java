package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to deserialization
 * of the Arguments type.
 */
public class InvalidArgsTypeException extends RuntimeException {

    public InvalidArgsTypeException(String message) {
        super(message);
    }
}
