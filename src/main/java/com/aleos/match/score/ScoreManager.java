package com.aleos.match.score;

import com.aleos.match.enums.Player;

public interface ScoreManager<T> {

    void setScore(Player player, T score);

    T getScore(Player player);

    T awardPoint(Player player);
}
