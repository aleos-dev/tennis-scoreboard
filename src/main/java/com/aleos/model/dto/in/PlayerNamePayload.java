package com.aleos.model.dto.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlayerNamePayload (

    @NotNull(message = "Name cannot be null.")
    @Size(min = 5, max = 50, message = "The name must be between {min} and {max}.")
    String name
 ) {
}
