package com.aleos.model.dto.out;

public record PlayerDto(

        String name,

        String country,

        String matchesEndpoint,

        String ongoingMatchUuid
) {
}
