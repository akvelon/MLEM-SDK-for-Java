package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to deserialization
 * of the RecordSet type property.
 */
public class NotSupportedTypeException extends RuntimeException {

    public NotSupportedTypeException(String message) {
        super(message);
    }
}
