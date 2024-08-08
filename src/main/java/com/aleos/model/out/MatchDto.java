package com.aleos.model.out;

import com.aleos.model.entity.Player;
import com.aleos.model.enums.MatchStatus;

import java.util.UUID;

public class MatchDto {

    private UUID id;

    private Player playerOne;

    private Player playerTwo;

    private MatchStatus status;
}
