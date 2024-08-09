package com.aleos.exception;

public class DaoOperationException extends RuntimeException {

    public DaoOperationException(String message) {
        super(message);
    }

    public DaoOperationException(String s, Exception e) {
    }
}
