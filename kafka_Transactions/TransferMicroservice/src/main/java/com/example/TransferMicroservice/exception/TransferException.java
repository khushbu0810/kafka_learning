package com.example.TransferMicroservice.exception;

public class TransferException extends RuntimeException{
    public TransferException(Throwable cause){
        super(cause);
    }

    public TransferException(String message){
        super(message);
    }
}
