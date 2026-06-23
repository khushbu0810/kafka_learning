package com.example.EmailNotificationMicroservice.exception;

public class RetryableException extends RuntimeException{
    public RetryableException(Throwable cause) {
        super(cause);
    }

    //custom Error message
    public RetryableException(String message) {
        super(message);
    }
}
