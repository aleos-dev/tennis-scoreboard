package com.aleos.model.out;

import java.util.List;

public record PlayersDto (

    List<PlayerDto> content,

    int page,
    int size,
    int totalPages,
    long totalItems,
    boolean hasNext,
    boolean hasPrevious
) {
}
