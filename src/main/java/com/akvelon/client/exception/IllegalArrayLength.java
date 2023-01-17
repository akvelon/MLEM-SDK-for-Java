package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to illegal array length.
 */
public class IllegalArrayLength extends RuntimeException {
    public IllegalArrayLength(String message) {
        super(message);
    }
}
