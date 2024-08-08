package com.aleos.model.out;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayerDto(

        String name,

        String country,

        String playerImageUrl,

        String countryImageUrl,

        @JsonProperty("matches")
        String matchesEndpoint
) {
}
