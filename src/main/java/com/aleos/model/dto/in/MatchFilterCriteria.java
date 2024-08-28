package com.aleos.model.dto.in;

import com.aleos.validation.ValidMatchStatus;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.Instant;


public record MatchFilterCriteria(

        @ValidMatchStatus(allowedValues = {"ongoing", "finished", "any"})
        String status,

        @Size( max = 50, message = "The name must not exceed {max}.")
        String playerName,

        @PastOrPresent(message = "Timestamp must be in the past or present.")
        Instant before
) {
}