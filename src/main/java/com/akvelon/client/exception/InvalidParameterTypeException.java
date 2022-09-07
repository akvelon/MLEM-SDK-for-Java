package com.akvelon.client.exception;

/**
 * A class that provides the illegal parameter type exception.
 */
public class InvalidParameterTypeException extends RuntimeException {

    public InvalidParameterTypeException(String message) {
        super(message);
    }
}
