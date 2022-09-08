package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to validation
 * of the Record property.
 */
public class IllegalRecordException extends RuntimeException {

    public IllegalRecordException(String message) {
        super(message);
    }
}
