package com.aleos.match.scoring.strategy;

import com.aleos.match.TestUtil;
import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.PointValue;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.score.manager.PointScoreManager;
import com.aleos.match.stage.Game;
import com.aleos.match.stage.StandardGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.aleos.match.TestUtil.getScore;
import static com.aleos.match.TestUtil.simulateGame;
import static com.aleos.match.model.enums.PointValue.*;
import static org.junit.jupiter.api.Assertions.*;

class StandardGameScoringStrategyTest {

    private Game<PointScoreManager> game;

    private StandardGameScoringStrategy strategy;

    private Player player;

    private Player opponent;

    @BeforeEach
    void setUp() {
        strategy = new StandardGameScoringStrategy();
        game = new StandardGame<>(strategy, new PointScoreManager());
        player = TestUtil.randomPlayer();
        opponent = player.getOpponent();
    }

    @Test
    void scorePoint_shouldStartValueBeZero_whenNewGameIsTested() {
        PointValue initialScoreOne = getScore(game, player);
        PointValue initialScoreTwo = getScore(game, opponent);

        assertSame(ZERO, initialScoreOne, "Initial score for player should be zero.");
        assertSame(ZERO, initialScoreTwo, "Initial score for opponent should be zero.");
    }

    @Test
    void scorePoint_shouldIncreasePoint_whenPlayerWinPoint() {
        strategy.scorePoint(game, player);

        PointValue newScore = getScore(game, player);
        assertEquals(FIFTEEN, newScore, "Player should have won one game.");
    }


    @Test
    void scorePoint_shouldSetWinner_whenPlayerWin() {
        // Simulate player winning 4 games
        simulateGame(game, Collections.nCopies(4, player));

        Player currentWinner = game.getWinner();
        assertSame(player, currentWinner, "Player should be the winner of the set.");
    }

    @Test
    void scorePoint_shouldNotWin_whenThereIsNotEnoughAdvantage() {
        simulateGame(game, Collections.nCopies(3, player));
        simulateGame(game, Collections.nCopies(4, opponent));

        Player currentWinner = game.getWinner();
        assertNull(currentWinner, "There should not be the winner.");
    }

    @Test
    void scorePoint_shouldNotEnd_whenThereIsNotEnoughAdvantage() {
        TestUtil.setGameScore(game, FORTY, FORTY);

        TestUtil.simulateGame(game, List.of(player, opponent, opponent, player, opponent, player, player));

        assertNull(game.getWinner());
        assertSame(StageState.ONGOING, game.getState(), "The game should be going.");

        strategy.scorePoint(game, player);

        assertSame(player, game.getWinner(), "There should not be the winner.");
        assertSame(StageState.FINISHED, game.getState(), "The game should be going.");
    }

    @Test
    void scorePoint_shouldDoNothing_whenWinnerWasDetermined() {
        simulateGame(game, Collections.nCopies(4, opponent));
        StageState finishedState = game.getState();

        strategy.scorePoint(game, opponent);

        assertEquals(StageState.FINISHED, finishedState);
        assertSame(finishedState, game.getState(), "The game state should not be changed.");
    }

    @Test
    void scorePoint_shouldMakeAdvantage_whenBothPlayersHaveForty() {
        TestUtil.setGameScore(game, FORTY, FORTY);

        strategy.scorePoint(game, player);

        assertEquals(PointValue.ADVANTAGE, getScore(game, player), "Player should make ADVANTAGE score at the winning point during deuce.");
    }

    @Test
    void scorePoint_shouldMakeForty_whenOpponentHasAdvantage() {
        TestUtil.setGameScore(game, FORTY, ADVANTAGE);

        strategy.scorePoint(game, Player.ONE);

        assertEquals(FORTY, getScore(game, Player.ONE));
        assertEquals(FORTY, getScore(game, Player.TWO), "Deuce should be settled when player caught up with opponent.");
    }
}