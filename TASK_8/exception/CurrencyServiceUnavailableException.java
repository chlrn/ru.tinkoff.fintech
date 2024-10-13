package com.example.rutinkofffintech.TASK_8.exception;

public class CurrencyServiceUnavailableException extends RuntimeException {

    public CurrencyServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
