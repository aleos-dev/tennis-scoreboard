package com.aleos.match.scoring.strategy;

import com.aleos.match.model.enums.Player;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.stage.TennisGame;

public class PingPongGameScoringStrategy implements ScoringStrategy<TennisGame> {

    private static final int MIN_GAME_COUNT_TO_WIN = 11;

    private static final int MIN_ADVANTAGE_TO_WIN = 2;

    @Override
    public void scorePoint(TennisGame game, Player player) {
        var manager = game.getScoreManager();
        var playerScore = manager.getScore(player) + 1;
        var opponentScore = manager.getScore(player.getOpponent());

        if (isWinningConditionMet(playerScore, opponentScore)) {
            concludeStage(game, player);
        }

        manager.awardScore(game.getState(), player);
    }

    protected boolean isWinningConditionMet(int playerScore, int opponentScore) {
        return hasEnoughScoreToWin(playerScore, MIN_GAME_COUNT_TO_WIN)
               && hasMinAdvantageToWin(playerScore, opponentScore, MIN_ADVANTAGE_TO_WIN);
    }
}
