package com.aleos.validation;

import com.aleos.util.PropertiesUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.*;
import java.util.stream.Collectors;

public class MatchFormatValidator implements ConstraintValidator<ValidMatchFormat, String> {

    private final Set<String> supportedFormats = PropertiesUtil.get("match.support.format")
            .map(v -> Arrays.stream(v.split(","))
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet())
            ).orElse(Set.of("bo3"));

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return supportedFormats.contains(value.toLowerCase(Locale.ROOT));
    }
}
