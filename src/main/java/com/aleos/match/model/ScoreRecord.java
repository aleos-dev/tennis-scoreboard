package com.aleos.match.model;

import com.aleos.match.model.enums.StageState;

public record ScoreRecord(StageState state, int p1, int p2) {

    public static ScoreRecord of(StageState state, int playerOneScore, int playerTwoScore) {
        return new ScoreRecord(state, playerOneScore, playerTwoScore);
    }
}
