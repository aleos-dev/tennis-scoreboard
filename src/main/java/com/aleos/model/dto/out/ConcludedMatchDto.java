package com.aleos.model.dto.out;

import com.aleos.model.entity.MatchInfo;
import com.aleos.model.enums.MatchStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ConcludedMatchDto extends MatchDto {

    private final String winner;

    private final MatchInfo info;

    public ConcludedMatchDto(UUID id,
                             String p1,
                             String p2,
                             MatchStatus status,
                             String winner,
                             MatchInfo info,
                             LocalDateTime concludedAt) {
        super(id, p1, p2, status, concludedAt);
        this.winner = winner;
        this.info = info;
    }
}
