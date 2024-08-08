package com.aleos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = FilePathValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface ValidFilePath {

    String message() default "Invalid file path";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
