package com.aleos.match.scoring;

import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.stage.Stage;

public interface ScoringStrategy<T extends Stage> {

    void scorePoint(T stage, Player player);

    default boolean hasEnoughScoreToWin(int playerScore, int minScoreToWin) {
        return playerScore >= minScoreToWin;
    }

    default boolean hasMinAdvantageToWin(int playerScore, int opponentScore, int minAdvantageToWin) {
        int diff = playerScore - opponentScore;
        return diff >= minAdvantageToWin;
    }

    default boolean isStageFinished(StageState state) {
        return state == StageState.FINISHED;
    }

    default boolean isTieBreak(StageState state) {
        return state == StageState.TIE_BREAK;
    }

    default void concludeStage(Stage stage, Player player) {
        stage.setWinner(player);
        stage.setState(StageState.FINISHED);
    }
}
