package com.aleos.exception;

public class ImageServiceException extends RuntimeException {

    public ImageServiceException(String formatted, Exception e) {
        super(formatted, e);
    }

    public ImageServiceException(String formatted) {
        super(formatted);
    }
}
