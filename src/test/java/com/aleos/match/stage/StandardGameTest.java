package com.aleos.match.stage;

import com.aleos.match.TestUtil;
import com.aleos.match.creation.StrategyProvider;
import com.aleos.match.exception.StageIsOverException;
import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.model.enums.StageType;
import com.aleos.match.scoremanager.StageScoreManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.aleos.match.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class StandardGameTest {

    private TennisGame game;

    private Player player;

    private Player opponent;

    @BeforeEach
    void setUp() {
        game = new StandardGame(StrategyProvider::getTennisGameStrategy, () -> new StageScoreManager(StageType.GAME));
        player = TestUtil.randomPlayer();
        opponent = player.getOpponent();
    }

    @Test
    void scorePoint_shouldStartValueBeZero_whenNewGameIsTested() {
        int initialScoreOne = getScore(game, player);
        int initialScoreTwo = getScore(game, opponent);

        assertSame(0, initialScoreOne, "Initial score for player should be zero.");
        assertSame(0, initialScoreTwo, "Initial score for opponent should be zero.");
    }

    @Test
    void scorePoint_shouldIncreasePoint_whenPlayerWinPoint() {
        game.scorePoint(player);

        int newScore = getScore(game, player);
        assertEquals(1, newScore, "Player should have won one point.");
    }


    @Test
    void scorePoint_shouldDeclareWinner_whenPlayerWin4InRow() {
        // Simulate player winning 4 games
        simulateStage(game, Collections.nCopies(4, player));

        Player currentWinner = game.getWinner();
        assertSame(player, currentWinner, "Player should be the winner of the set.");
    }

    @Test
    void scorePoint_shouldNotWin_whenThereIsNotEnoughAdvantage() {
        setScore(game, 3, 3);

        game.scorePoint(opponent);

        Player currentWinner = game.getWinner();
        assertNull(currentWinner, "There should not be the winner.");
    }

    @Test
    void scorePoint_shouldNotEnd_whenThereIsNotEnoughAdvantage() {
        setScore(game, 3, 3);

        simulateScore(game, List.of(player, opponent, opponent, player, opponent, player, player));

        assertNull(game.getWinner(), "There should not be the winner.");
        assertSame(StageState.ONGOING, game.getState(), "The game should be going.");

        game.scorePoint(player);

        assertSame(player, game.getWinner(), "The winner should be declared.");
        assertSame(StageState.FINISHED, game.getState(), "The game should be finished.");
    }

    @Test
    void scorePoint_shouldThrowException_whenWinnerWasDetermined() {
        simulateScore(game, Collections.nCopies(4, opponent));

        assertThrows(
                StageIsOverException.class,
                () -> game.scorePoint(opponent),
                "Invalid method call, should throw StageIsOverException.");
    }

    @Test
    void scorePoint_shouldBeDeuce_whenBothPlayersHave3Points() {
        simulateScore(game, Collections.nCopies(3, player));
        simulateScore(game, Collections.nCopies(3, opponent));

        assertSame(StageState.DEUCE, game.getState(), "The game state should be DEUCE when both players have 3 points.");
    }

    @Test
    void scorePoint_shouldBeNoDeuce_whenPlayerGotAdvantage() {
        simulateScore(game, Collections.nCopies(3, player));
        simulateScore(game, Collections.nCopies(3, opponent));

        game.scorePoint(player);

        assertSame(StageState.ONGOING, game.getState(), "The game state should be ONGOING when player got advantage.");
    }

    @Test
    void scorePoint_shouldBeDeuce_whenBothPlayersHave4Points() {
        simulateScore(game, Collections.nCopies(3, player));
        simulateScore(game, Collections.nCopies(3, opponent));

        game.scorePoint(player);
        game.scorePoint(opponent);

        assertSame(StageState.DEUCE, game.getState(), "The game state should be DEUCE when both players have 4 points");
    }

    @Test
    void scorePoint_shouldMakeForty_whenOpponentHasAdvantage() {
        setScore(game, 3, 4);

        game.scorePoint(Player.ONE);

        assertEquals(3, getScore(game, Player.ONE), "Deuce should be settled when player caught up with opponent.");
        assertEquals(3, getScore(game, Player.TWO), "Deuce should be settled when player caught up with opponent.");
        assertSame(StageState.DEUCE, game.getState(), "The game state should be DEUCE when both players have 4 points");
    }

    @Test
    void scorePoint_shouldWinOn7PointsInRow_whenIsTieBreak() {
        game.setState(StageState.TIE_BREAK);
        simulateScore(game, Collections.nCopies(6, player));

        assertNull(game.getWinner(), "There should not be the winner.");
        assertSame(StageState.TIE_BREAK, game.getState(), "The game should be going.");

        game.scorePoint(player);

        assertSame(player, game.getWinner(), "The winner should be declared.");
        assertSame(7, game.getScoreManager().getScore(player), "The winner should have 7 points.");
        assertSame(StageState.FINISHED, game.getState(), "The game should be finished.");

    }
}