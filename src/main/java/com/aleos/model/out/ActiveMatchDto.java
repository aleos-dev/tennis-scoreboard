package com.aleos.model.out;

import com.aleos.model.MatchScore;
import com.aleos.model.enums.MatchStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class ActiveMatchDto extends MatchDto {

    private final MatchScore score;

    private final LocalTime startedAt;

    public ActiveMatchDto(UUID id, String p1, String p2, MatchStatus status, MatchScore score, LocalTime startedAt) {
        super(id, p1, p2, status);
        this.score = score;
        this.startedAt = startedAt;
    }
}
