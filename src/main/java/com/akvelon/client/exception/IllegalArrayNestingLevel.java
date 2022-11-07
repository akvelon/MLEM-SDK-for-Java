package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to validation
 * of the array nesting level.
 */
public class IllegalArrayNestingLevel extends RuntimeException {
    public IllegalArrayNestingLevel(String message) {
        super(message);
    }
}
