package com.aleos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SortByValidator implements ConstraintValidator<ValidSortBy, String> {

    private String[] allowedValues;

    @Override
    public void initialize(ValidSortBy constraintAnnotation) {
        allowedValues =  constraintAnnotation.allowedValues();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (var val: allowedValues) {
            if (val.equalsIgnoreCase(value)) {
                return true;
            }
        }

        context.buildConstraintViolationWithTemplate(
                "sortBy must be one of '" + String.join("', '", allowedValues) + "'."
        ).addConstraintViolation();

        return false;
    }
}
