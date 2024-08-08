package com.aleos.model.in;

import com.aleos.validation.BasicGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PlayerNamePayload {

    @NotNull(message = "Name cannot be null.")
    @Size(min = 5, max = 50, groups = BasicGroup.class, message = "The name must be between {min} and {max}.")
    String name;
}
