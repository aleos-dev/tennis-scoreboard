package com.aleos.match.strategy;

import com.aleos.match.enums.Player;
import com.aleos.match.enums.PointValue;
import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Game;
import com.aleos.match.score.PointScoreManager;

import static com.aleos.match.enums.PointValue.ADVANTAGE;
import static com.aleos.match.enums.PointValue.FORTY;

public class StandardGameScoringStrategy implements ScoringStrategy<Game<PointScoreManager>> {

    @Override
    public void scorePoint(Game<PointScoreManager> game, Player player) {
        var manager = game.getScoreManager();
        var playerScore = manager.getScore(player);
        var opponentScore = manager.getScore(player.getOpponent());

        if (isWinningConditionMet(playerScore, opponentScore)) {
            game.setWinner(player);
            return;
        }
        manager.awardPoint(player);

        handleDeuceCondition(manager, playerScore, opponentScore);
    }

    private boolean isWinningConditionMet(PointValue playerScore, PointValue opponentScore) {
        return playerScore == ADVANTAGE || isCommonWin(playerScore, opponentScore);
    }

    private boolean isCommonWin(PointValue playerScore, PointValue opponentScore) {
        return playerScore == FORTY && opponentScore.ordinal() < FORTY.ordinal();
    }

    private void handleDeuceCondition(PointScoreManager manager, PointValue playerScore, PointValue opponentScore) {
        if (
                (playerScore == FORTY && opponentScore == FORTY)
                || (playerScore == ADVANTAGE && opponentScore == ADVANTAGE)
        ) {
            manager.setScore(Player.ONE, FORTY);
            manager.setScore(Player.TWO, FORTY);
        }
    }
}
