package com.aleos.match.stage;

import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
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
