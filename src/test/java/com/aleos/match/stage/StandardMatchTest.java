package com.aleos.match.stage;

import com.aleos.match.TestUtil;
import com.aleos.match.creation.StageFactory;
import com.aleos.match.model.enums.MatchFormat;
import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.score.manager.NumericScoreManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.aleos.match.TestUtil.getScore;
import static com.aleos.match.TestUtil.simulateSet;
import static org.junit.jupiter.api.Assertions.*;

class StandardMatchTest {

    private Match<NumericScoreManager> match;
    private Player player;
    private Player opponent;

    @BeforeEach
    void setUp() {
        match = StageFactory.createBo3Match();
        player = TestUtil.randomPlayer();
        opponent = player.getOpponent();
    }

    @Test
    void standardMatch_shouldStartWithInitialConditions() {
        assertEquals(0, getScore(match, player), "Initial score for player should be zero.");
        assertEquals(0, getScore(match, opponent), "Initial score for opponent should be zero.");
        assertNull(match.getWinner(), "Initial winner should be null.");
        assertEquals(MatchFormat.BO3, ((StandardMatch<NumericScoreManager, TennisSet<NumericScoreManager>>) match).getMatchFormat(), "Match format should be Best of 3.");
    }

    @Test
    void standardMatch_shouldHandleScoringAndUpdateSetsCorrectly() {
        simulateSet(match, List.of(player));
        assertNull(match.getWinner(), "No winner should be declared after winning a set.");

        simulateSet(match, List.of(player));
        assertEquals(player, match.getWinner(), "Player should be declared winner after winning two sets.");
    }

    @Test
    void standardMatch_shouldHandleBestOfFiveFormatCorrectly() {
        // Create a new match with Best of 5th format
        match = StageFactory.createBo5Match();
        assertEquals(MatchFormat.BO5, ((StandardMatch<NumericScoreManager, TennisSet<NumericScoreManager>>) match).getMatchFormat(), "Match format should be Best of 5.");

        // Simulate winning two sets should not declare winner

        simulateSet(match, Collections.nCopies(2, player));
        assertNull(match.getWinner(), "No winner should be declared after winning two sets in a Best of 5 match.");

        // Win the third set to win the match
        simulateSet(match, List.of(player));
        assertEquals(player, match.getWinner(), "Player should be declared winner after winning three sets in a Best of 5 match.");
    }

    @Test
    void standardMatch_shouldSetWinner_WhenMatchIsOver() {
        simulateSet(match, Collections.nCopies(2, player));

        assertTrue(match.isOver(), "The match should be over");
        assertEquals(StageState.FINISHED, match.getState(), "The match should have the state FINISHED.");
    }
}

