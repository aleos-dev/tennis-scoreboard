package com.aleos.match.score;

import com.aleos.match.model.enums.Player;

public interface ScoreManager<S> {

    void setScore(Player player, S score);

    S getScore(Player player);

    S awardPoint(Player player);
}
