package com.aleos.util;

import com.aleos.exception.PropertiesLoadingException;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public final class PropertiesUtil {

    private static final Properties props = new Properties();

    static {
        try {
            props.load(PropertiesUtil.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new PropertiesLoadingException("Failed to load properties file", e);
        }
    }

    private PropertiesUtil() {
        throw new UnsupportedOperationException("Util class can not be instantiated");
    }

    public static Optional<String> get(String key) {
        return Optional.ofNullable(props.getProperty(key));
    }
}
