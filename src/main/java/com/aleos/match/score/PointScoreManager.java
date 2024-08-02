package com.aleos.match.score;

import com.aleos.match.enums.Player;
import com.aleos.match.enums.PointValue;

import java.util.EnumMap;
import java.util.Map;

public class PointScoreManager implements ScoreManager<PointValue> {

    private final Map<Player, PointValue> scores = new EnumMap<>(Player.class);

    public PointScoreManager() {
        scores.putIfAbsent(Player.ONE, PointValue.ZERO);
        scores.putIfAbsent(Player.TWO, PointValue.ZERO);
    }

    @Override
    public void setScore(Player player, PointValue score) {
        scores.put(player, score);
    }

    @Override
    public PointValue getScore(Player player) {
        return scores.get(player);
    }

    @Override
    public PointValue awardPoint(Player player) {
        return scores.computeIfPresent(player, (k, v) -> v.increase());
    }
}
