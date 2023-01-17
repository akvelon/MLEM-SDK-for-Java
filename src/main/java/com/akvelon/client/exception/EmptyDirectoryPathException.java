package com.akvelon.client.exception;

/**
 * Specialized class specifically used to indicate problems related to empty directory.
 */
public class EmptyDirectoryPathException extends RuntimeException {
    public EmptyDirectoryPathException(String message) {
        super(message);
    }
}
