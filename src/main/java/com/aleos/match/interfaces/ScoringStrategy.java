package com.aleos.match.interfaces;

import com.aleos.match.enums.Player;
import com.aleos.match.interfaces.stage.Stage;

public interface ScoringStrategy<T extends Stage<?>> {

    void scorePoint(T stage, Player player);


    default boolean hasEnoughScoreToWin(int playerScore, int minScoreToWin) {
        return playerScore >= minScoreToWin;
    }

    default boolean hasMinAdvantageToWin(int wonByPlayer, int wonByOpponent, int minAdvantageToWin) {
        int diff = wonByPlayer - wonByOpponent;
        return diff >= minAdvantageToWin;
    }
}
