package com.aleos.model.in;

import com.aleos.validation.ValidMatchStatus;
import jakarta.validation.constraints.Size;


public record MatchFilterCriteria(

        @ValidMatchStatus(allowedValues = {"ongoing", "finished", "any"})
        String status,

        @Size(min = 5, max = 50, message = "The name must be between {min} and {max}.")
        String playerName
) {
}