package com.aleos.model.in;

import com.aleos.validation.BasicGroup;
import com.aleos.validation.ValidCountryCode;
import jakarta.servlet.http.Part;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PlayerPayload {

    @NotNull(message = "Name cannot be null.")
    @Size(min = 5, max = 50, groups = BasicGroup.class, message = "The name must be between {min} and {max}.")
    String name;

    @ValidCountryCode
    String country;

    Part image;
}
