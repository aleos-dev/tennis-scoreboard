package com.aleos.model.out;

import java.util.List;

public record MatchesDto (

    List<MatchDto> content,

    int page,
    int totalPages,
    int totalItems
 ) {
}
