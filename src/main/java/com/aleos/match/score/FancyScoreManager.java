package com.aleos.match.score;

import com.aleos.match.enums.FancyPoint;
import com.aleos.match.enums.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;

@Getter
@Setter
public class FancyScoreManager implements ScoreManager<FancyPoint> {

    protected final Map<Player, FancyPoint> scores = new EnumMap<>(Player.class);

    public FancyScoreManager() {
        scores.putIfAbsent(Player.ONE, FancyPoint.LOVE);
        scores.putIfAbsent(Player.TWO, FancyPoint.LOVE);
    }

    @Override
    public void setScore(Player player, FancyPoint score) {
        scores.put(player, score);
    }

    @Override
    public FancyPoint getScore(Player player) {
        return scores.get(player);
    }

    @Override
    public FancyPoint awardPoint(Player player) {
        return scores.computeIfPresent(player, (k, v) -> v.increase());
    }
}
