package com.aleos.exception;

public class BeanInitializationException extends RuntimeException {

    public BeanInitializationException(String message) {
        super(message);
    }

    public BeanInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
