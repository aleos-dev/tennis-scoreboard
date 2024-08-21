package com.aleos.exception;

public class JspForwardingException extends RuntimeException {

    public JspForwardingException(String formatted, Exception e) {
        super(formatted, e);
    }
}
