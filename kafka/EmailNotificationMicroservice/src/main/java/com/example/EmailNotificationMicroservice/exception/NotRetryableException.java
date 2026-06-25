package com.example.EmailNotificationMicroservice.exception;
/*
-> logic written in KafkaConsumerConfig -> DefaultErrorHandler
-> exception occur when event takes place -> productCreatedEvent
 */
public class NotRetryableException extends RuntimeException{
    public NotRetryableException(Throwable cause) {
        super(cause);
    }

    public NotRetryableException(String message) {
        super(message);
    }
}
