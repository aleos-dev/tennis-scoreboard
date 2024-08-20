package com.aleos.model.dto.in;

import com.aleos.validation.ValidCountryCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlayerPayload(

        @NotNull(message = "Name cannot be null.")
        @Size(min = 5, max = 50, message = "The name must be between {min} and {max}.")
        String name,

        @ValidCountryCode
        String country,

        String imageUrl
) {
}
