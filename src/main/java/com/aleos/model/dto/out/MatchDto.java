package com.aleos.model.dto.out;

import com.aleos.model.enums.MatchStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class MatchDto {

    protected final UUID id;

    protected final String playerOne;

    protected final String playerTwo;

    protected final MatchStatus status;

    protected final LocalDateTime eventDateTime;
}
