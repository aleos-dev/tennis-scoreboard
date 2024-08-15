package com.aleos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DifferentPlayersValidator.class)
public @interface DifferentPlayers {
    String message() default "Player names must be unique.";

    Class<?>[] group() default {};

    Class<? extends Payload>[] payload() default {};
}
