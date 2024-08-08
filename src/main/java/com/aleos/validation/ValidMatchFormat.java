package com.aleos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = MatchFormatValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface ValidMatchFormat {

    String message() default "Invalid match format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
