package com.aleos.match.interfaces.stage;

import com.aleos.match.enums.Player;
import com.aleos.match.enums.StageState;
import com.aleos.match.score.ScoreManager;

import java.beans.PropertyChangeListener;

public interface Stage<S extends ScoreManager<?>> {

    void scorePoint(Player player);

    S getScoreManager();

    boolean isOver();

    Player getWinner();

    void setWinner(Player player);

    void addPropertyChangeListener(PropertyChangeListener listener);

    StageState getState();

    void setState(StageState state);
}
