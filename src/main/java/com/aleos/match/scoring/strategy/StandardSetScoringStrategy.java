package com.aleos.match.scoring.strategy;

import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.stage.TennisSet;
import com.aleos.match.score.manager.NumericScoreManager;

public class StandardSetScoringStrategy implements ScoringStrategy<TennisSet<NumericScoreManager>> {

    private static final int MIN_WON_GAME_COUNT_TO_WIN = 6;

    private static final int TIE_BREAK_GAME_COUNT = 6;

    private static final int MIN_ADVANTAGE_TO_WIN = 2;

    @Override
    public void scorePoint(TennisSet<NumericScoreManager> set, Player player) {
        var stageState = set.getState();
        var manager = set.getScoreManager();
        manager.awardPoint(player);

        var playerScore = manager.getScore(player);
        var opponentScore = manager.getScore(player.getOpponent());

        if (tieBreakTrigger(stageState, playerScore, opponentScore)) {
            initTieBreak(set, manager);
            return;
        }

        if (isWinningConditionMet(stageState, playerScore, opponentScore)) {
            set.setWinner(player);
        }
    }

    private boolean isWinningConditionMet(StageState currentState, int playerScore, int opponentScore) {
        if (currentState == StageState.TIE_BREAK
            && hasMinAdvantageToWin(playerScore, opponentScore, MIN_ADVANTAGE_TO_WIN)) {
            return true;
        }
        return hasEnoughScoreToWin(playerScore, MIN_WON_GAME_COUNT_TO_WIN)
               && hasMinAdvantageToWin(playerScore, opponentScore, MIN_ADVANTAGE_TO_WIN);
    }

    private boolean tieBreakTrigger(StageState currentState, int wonByPlayer, int wonByOpponent) {
        return currentState != StageState.TIE_BREAK
               && wonByPlayer == TIE_BREAK_GAME_COUNT
               && wonByOpponent == TIE_BREAK_GAME_COUNT;
    }

    private static void initTieBreak(TennisSet<NumericScoreManager> set, NumericScoreManager manager) {
        manager.setScore(Player.ONE, 0);
        manager.setScore(Player.TWO, 0);
        set.setState(StageState.TIE_BREAK);
    }
}
