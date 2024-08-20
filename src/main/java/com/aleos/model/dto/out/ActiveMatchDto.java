package com.aleos.model.dto.out;

import com.aleos.model.MatchScore;
import com.aleos.model.enums.MatchStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ActiveMatchDto extends MatchDto {

    private final MatchScore score;

    public ActiveMatchDto(UUID id, String p1, String p2, MatchStatus status, MatchScore score, LocalDateTime startedAt) {
        super(id, p1, p2, status, startedAt);
        this.score = score;
    }
}
