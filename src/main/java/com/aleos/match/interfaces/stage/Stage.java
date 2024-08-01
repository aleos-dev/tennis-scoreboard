package com.aleos.match.interfaces.stage;

import com.aleos.match.enums.Player;
import com.aleos.match.score.Score;

import java.beans.PropertyChangeListener;
import java.util.Map;

public interface Stage<T extends Score<?>> {

    void scorePoint(Player player);

    Map<Player, T> getScores();

    boolean isOver();

    void setWinner(Player player);

    void addPropertyChangeListener(PropertyChangeListener listener);
}
