package com.aleos.model.in;

import java.time.Instant;

public record PageablePayload (
        int page,
        int limit,
        String sortBy,
        String sortDirection,
        Instant lastTimestamp
) {
}