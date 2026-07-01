package com.example.core.exception;

public class CreditCardProcessorUnavailableException extends RuntimeException {
    public CreditCardProcessorUnavailableException(Throwable cause) {
        super(cause);
    }
}
