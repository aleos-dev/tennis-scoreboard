package com.aleos.model.dto.out;

import java.util.List;

public record MatchesDto (

    List<MatchDto> content,

    int page,
    int size,
    int totalPages,
    long totalItems,
    boolean hasNext,
    boolean hasPrevious

 ) {
}