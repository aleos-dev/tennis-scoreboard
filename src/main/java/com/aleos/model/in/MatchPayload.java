package com.aleos.model.in;

import com.aleos.validation.DifferentPlayers;
import com.aleos.validation.ValidMatchFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@DifferentPlayers
public record MatchPayload(

        @NotNull(message = "First player name cannot be null.")
        @Size(min = 5, max = 50, message = "The name must be between {min} and {max}.")
        String playerOneName,

        @NotNull(message = "Second player name cannot be null.")
        @Size(min = 5, max = 50, message = "The name must be between {min} and {max}.")
        String playerTwoName,

        @ValidMatchFormat
        String format
) {
}
