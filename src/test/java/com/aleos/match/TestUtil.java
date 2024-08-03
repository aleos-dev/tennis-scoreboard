package com.aleos.match;

import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.PointValue;
import com.aleos.match.score.manager.NumericScoreManager;
import com.aleos.match.score.manager.PointScoreManager;
import com.aleos.match.stage.Game;
import com.aleos.match.stage.Match;
import com.aleos.match.stage.TennisSet;

import java.util.List;
import java.util.Random;

public final class TestUtil {

    private static final Random RANDOM = new Random();

    private TestUtil() {
        throw new UnsupportedOperationException("Util class, can not be instantiated");
    }

    public static Player randomPlayer() {
        return Player.values()[RANDOM.nextInt(Player.values().length)];
    }

    public static void setGameScore(Game<PointScoreManager> game, PointValue playerOneScore, PointValue playerTwoScore) {
        var manager = game.getScoreManager();
        manager.setScore(Player.ONE, playerOneScore);
        manager.setScore(Player.TWO, playerTwoScore);
    }

    public static PointValue getScore(Game<PointScoreManager> game, Player player) {
        return game.getScoreManager().getScore(player);
    }

    public static Integer getScore(TennisSet<NumericScoreManager> set, Player player) {
        return set.getScoreManager().getScore(player);
    }

    public static Integer getScore(Match<NumericScoreManager> match, Player player) {
        return match.getScoreManager().getScore(player);
    }

    public static void simulateGame(Game<PointScoreManager> game, List<Player> pointWinners) {
        for (Player player : pointWinners) {
            if (!game.isOver()) {
                game.scorePoint(player);
            }
        }
    }

    public static void simulateGame(TennisSet<NumericScoreManager> set, List<Player> pointWinners) {
        for (Player player : pointWinners) {
            for (int i = 0; i < 4; i++) {
                set.scorePoint(player);
            }
        }
    }

    public static void simulateSet(TennisSet<NumericScoreManager> set, List<Player> setWinners) {
        for (Player player : setWinners) {
            while (!set.isOver()) {
                set.scorePoint(player);
            }
        }
    }

    public static void simulateSet(Match<NumericScoreManager> match, List<Player> setWinners) {
        for (Player player : setWinners) {
            for (int i = 0; i < 24; i++) {
                match.scorePoint(player);
            }
        }
    }
}
