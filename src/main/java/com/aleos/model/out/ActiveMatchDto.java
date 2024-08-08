package com.aleos.model.out;

import com.aleos.model.MatchScore;

import java.time.LocalTime;

public class ActiveMatchDto extends MatchDto {

    private MatchScore score;

    private LocalTime startedAt;
}
