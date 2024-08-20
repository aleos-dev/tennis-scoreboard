package com.aleos.model.dto.in;

import com.aleos.validation.ValidCountryCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record PlayerFilterCriteria(

        @ValidCountryCode
        String country,

        @Size(min = 3, max = 50, message = "The name must be between {min} and {max}.")
        String name,

        @PastOrPresent(message = "Timestamp must be in the past or present.")
        Instant instant
) {
}
