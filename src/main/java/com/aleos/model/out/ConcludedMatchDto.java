package com.aleos.model.out;

import com.aleos.model.entity.MatchInfo;
import com.aleos.model.enums.MatchStatus;

import java.time.LocalDate;
import java.util.UUID;

public class ConcludedMatchDto extends MatchDto {

    private final String winner;

    private final MatchInfo info;

    private final LocalDate concludedAt;

    public ConcludedMatchDto(UUID id, String p1, String p2, MatchStatus status, String winner, MatchInfo info, LocalDate concludedAt) {
        super(id, p1, p2, status);
        this.winner =  winner;
        this.info = info;
        this.concludedAt = concludedAt;
    }
}
