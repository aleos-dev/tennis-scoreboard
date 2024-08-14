package com.aleos.model.in;

import com.aleos.validation.ValidSortBy;
import jakarta.validation.constraints.*;

import java.time.Instant;

public record PageablePayload(

        @Min(value = 1, message = "Page should be at least {value}.")
        @Max(value = 255, message = "Page should be no more than {value}.")
        Integer page,

        @Min(value = 0, message = "Size should be at least {value}.")
        @Max(value = 255, message = "Size should be no more than {value}.")
        Integer size,

        @NotBlank(message = "Sort by field cannot be blank.")
        @ValidSortBy(allowedValues = {"concludedAt"})
        String sortBy,

        @Pattern(regexp = "(?i)asc|desc", message = "Sort direction must be either 'asc' or 'desc'.")
        String sortDirection,

        @NotNull(message = "Timestamp cannot be null.")
        @PastOrPresent(message = "Timestamp must be in the past or present.")
        Instant before
) {
}