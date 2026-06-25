package com.example.EmailNotificationMicroservice.exception;
/*
-> exception that can be recovered (network issue may be)
handled in kafkaConsumerConfig -> defaultErrorHandler
-> how much time to wait before sending message again -> FixedBackOff(noOfSecondsToWait , maxNoOfRetries)
 */
public class RetryableException extends RuntimeException{
    public RetryableException(Throwable cause) {
        super(cause);
    }

    //custom Error message
    public RetryableException(String message) {
        super(message);
    }
}
