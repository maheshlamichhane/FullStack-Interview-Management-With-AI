package com.notification.project.exception;

public class RetryableException extends RuntimeException {

    public RetryableException() {
    }
    public RetryableException(String message) {
        super(message);
    }
    public RetryableException(Throwable cause) {
        super(cause);
    }
}
