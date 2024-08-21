package com.aleos.model.dto.out;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class MatchesDto {

    List<MatchDto> content;

    int page;
    int size;
    int totalPages;
    long totalItems;
    boolean hasNext;
    boolean hasPrevious;
}
