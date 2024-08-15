package com.aleos.exception;

public class ResourceForwardingException extends RuntimeException {

    public ResourceForwardingException(String message, Exception e) {
        super(message, e);
    }
}
