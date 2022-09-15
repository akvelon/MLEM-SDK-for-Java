package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to validation
 * of the Record property.
 */
public class IllegalRecordTypeException extends RuntimeException {

    public IllegalRecordTypeException(String message) {
        super(message);
    }
}
