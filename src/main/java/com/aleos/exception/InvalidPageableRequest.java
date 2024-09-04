package com.aleos.exception;

public class InvalidPageableRequest extends RuntimeException {

    public InvalidPageableRequest(String formatted) {
        super(formatted);
    }
}
