package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems due to validating
 * a RecordSet type property.
 */
public class InvalidRecordSetTypeException extends RuntimeException {

    public InvalidRecordSetTypeException(String message) {
        super(message);
    }
}
