package com.aleos.match.scoring;

import com.aleos.match.model.enums.Player;
import com.aleos.match.score.ScoreManager;
import com.aleos.match.stage.Stage;

public interface ScoringStrategy<T extends Stage<? extends ScoreManager<?>>> {

    void scorePoint(T stage, Player player);


    default boolean hasEnoughScoreToWin(int playerScore, int minScoreToWin) {
        return playerScore >= minScoreToWin;
    }

    default boolean hasMinAdvantageToWin(int wonByPlayer, int wonByOpponent, int minAdvantageToWin) {
        int diff = wonByPlayer - wonByOpponent;
        return diff >= minAdvantageToWin;
    }
}
