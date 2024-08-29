package com.aleos.model.dto.in;

import com.aleos.validation.ValidCountryCode;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record PlayerFilterCriteria(

        @ValidCountryCode
        String country,

        @Size(max = 50, message = "The name must not exceed {max} characters.")
        String name,

        @PastOrPresent(message = "Timestamp must be in the past or present.")
        Instant before
) {
}