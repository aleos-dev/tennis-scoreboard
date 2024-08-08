package com.aleos.exception;

import java.io.IOException;

public class PropertiesLoadingException extends RuntimeException {

    public PropertiesLoadingException(String failedToLoadPropertiesFile, IOException e) {
    }
}
