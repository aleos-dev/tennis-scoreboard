package com.aleos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CountryCodeValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface ValidCountryCode {

    String message() default "Invalid country code. It should be in ISO 3166-1 alpha-2 standard.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
