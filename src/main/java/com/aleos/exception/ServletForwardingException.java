package com.aleos.exception;

public class ServletForwardingException extends RuntimeException {

    public ServletForwardingException(String formatted, Exception e) {
        super(formatted, e);
    }
}
