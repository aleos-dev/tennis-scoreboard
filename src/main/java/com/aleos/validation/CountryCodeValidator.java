package com.aleos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class CountryCodeValidator implements ConstraintValidator<ValidCountryCode, String> {

    private static final Set<String> COUNTRY_CODES;

    static {
        COUNTRY_CODES = Arrays.stream(Locale.getISOCountries()).collect(Collectors.toSet());
        COUNTRY_CODES.add("XX");
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return COUNTRY_CODES.contains(value.trim().toUpperCase());
    }
}
