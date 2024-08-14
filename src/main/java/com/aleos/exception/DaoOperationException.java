package com.aleos.exception;

public class DaoOperationException extends RuntimeException {

    public DaoOperationException(String message) {
        super(message);
    }

    public DaoOperationException(String message, Exception e) {
        super(message, e);
    }
}
