package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to deserialization
 * of the "values" property.
 */
public class InvalidValuesException extends RuntimeException {

    public InvalidValuesException(String message) {
        super(message);
    }
}
