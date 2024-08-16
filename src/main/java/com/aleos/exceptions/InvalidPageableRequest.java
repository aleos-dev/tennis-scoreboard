package com.aleos.exceptions;

public class InvalidPageableRequest extends RuntimeException {

    public InvalidPageableRequest(String formatted) {
        super(formatted);
    }
}
