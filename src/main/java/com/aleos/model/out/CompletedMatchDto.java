package com.aleos.model.out;

import com.aleos.model.entity.MatchInfo;
import com.aleos.model.entity.Player;

import java.time.LocalDate;

public class CompletedMatchDto extends MatchDto {

    private Player winner;

    private MatchInfo info;

    private LocalDate date;
}
