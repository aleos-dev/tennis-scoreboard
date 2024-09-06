package com.aleos.exception;

public class PropertiesLoadingException extends RuntimeException {

    public PropertiesLoadingException(String message, Exception e) {
        super(message, e);
    }
}
