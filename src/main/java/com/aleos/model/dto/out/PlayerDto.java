package com.aleos.model.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayerDto(

        String name,

        String country,

        @JsonProperty("matches")
        String matchesEndpoint,

        String ongoingMatchUuid
) {
}
