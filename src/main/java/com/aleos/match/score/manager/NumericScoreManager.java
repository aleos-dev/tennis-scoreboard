package com.aleos.match.score.manager;

import com.aleos.match.model.enums.Player;
import com.aleos.match.score.ScoreManager;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;

@Getter
@Setter
public class NumericScoreManager implements ScoreManager<Integer> {

    protected final Map<Player, Integer> scores = new EnumMap<>(Player.class);

    public NumericScoreManager() {
        scores.putIfAbsent(Player.ONE, 0);
        scores.putIfAbsent(Player.TWO, 0);
    }

    @Override
    public void setScore(Player player, Integer score) {
        scores.put(player, score);
    }

    @Override
    public Integer getScore(Player player) {
        return scores.get(player);
    }

    @Override
    public Integer awardPoint(Player player) {
        return scores.computeIfPresent(player, (k, v) -> v + 1);
    }
}
