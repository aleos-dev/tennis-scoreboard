package com.aleos.model.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayerDto(

        String name,

        String country,

        String playerImageUrl,

        String countryImageUrl,

        @JsonProperty("matches")
        String matchesEndpoint,

        String ongoingMatchUuid
) {
}
