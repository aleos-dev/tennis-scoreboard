package com.aleos.exception;

public class UniqueConstraintViolationException extends RuntimeException {

    public UniqueConstraintViolationException(String message, Exception e) {
        super(message, e);
    }
}
