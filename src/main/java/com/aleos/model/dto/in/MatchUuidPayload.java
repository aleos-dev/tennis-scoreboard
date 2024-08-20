package com.aleos.model.dto.in;

import lombok.NonNull;

import java.util.UUID;

public record MatchUuidPayload(

        @NonNull
        UUID id
) {
}
