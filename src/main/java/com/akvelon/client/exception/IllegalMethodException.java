package com.akvelon.client.exception;

/**
 * A class that provides the illegal method name exception.
 */
public class IllegalMethodException extends RuntimeException {

    public IllegalMethodException(String message) {
        super(message);
    }
}
