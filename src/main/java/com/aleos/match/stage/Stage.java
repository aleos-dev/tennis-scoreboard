package com.aleos.match.stage;

import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.scoremanager.ScoreManager;

import java.beans.PropertyChangeListener;
import java.util.Optional;

public interface Stage {

    void scorePoint(Player player);

    ScoreManager getScoreManager();

    boolean isOver();

    Player getWinner();

    void setWinner(Player player);

    void addPropertyChangeListener(PropertyChangeListener listener);

    StageState getState();

    void setState(StageState state);

    Optional<Stage> getChildStage();
}
