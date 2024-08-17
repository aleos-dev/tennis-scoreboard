package com.aleos.exception;

public class EntityNotFoundDbException extends RuntimeException {

    public EntityNotFoundDbException(String formatted) {
        super(formatted);
    }
}
