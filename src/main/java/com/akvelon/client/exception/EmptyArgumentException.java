package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to empty argument.
 */
public class EmptyArgumentException extends RuntimeException {
    public EmptyArgumentException(String message) {
        super(message);
    }
}
