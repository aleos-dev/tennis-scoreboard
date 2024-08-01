package com.aleos.match;

import com.aleos.match.enums.Player;
import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Game;
import com.aleos.match.interfaces.stage.Match;
import com.aleos.match.interfaces.stage.Stage;
import com.aleos.match.interfaces.stage.TennisSet;
import com.aleos.match.score.Score;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractStage<T extends Score<?>, S extends Stage<T>>
        implements Game<T>, TennisSet<T>, Match<T>, PropertyChangeListener {

    protected final ScoringStrategy<S> scoringStrategy;

    protected final PropertyChangeSupport support;

    protected final Map<Player, T> scores = new EnumMap<>(Player.class);

    protected Player winner;

    protected AbstractStage(ScoringStrategy<S> strategy) {
        this.scoringStrategy = strategy;
        this.support = new PropertyChangeSupport(this);
        initScores();
    }

    @Override
    public boolean isOver() {
        return winner != null;
    }

    @Override
    public void setWinner(Player player) {
        this.winner = player;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    protected abstract void initScores();

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }
}
