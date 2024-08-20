package com.aleos.validation;

import com.aleos.model.dto.in.MatchPayload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifferentPlayersValidator implements ConstraintValidator<DifferentPlayers, MatchPayload> {

    @Override
    public boolean isValid(MatchPayload value, ConstraintValidatorContext context) {
        return !(value == null || value.playerOneName().equalsIgnoreCase(value.playerTwoName()));
    }
}
