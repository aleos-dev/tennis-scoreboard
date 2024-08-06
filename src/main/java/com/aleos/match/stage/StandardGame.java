package com.aleos.match.stage;

import com.aleos.match.model.enums.MatchEvent;
import com.aleos.match.model.enums.Player;
import com.aleos.match.scoremanager.ScoreManager;
import com.aleos.match.scoring.ScoringStrategy;

import java.beans.PropertyChangeEvent;
import java.util.Optional;
import java.util.function.Supplier;

public class StandardGame extends AbstractStage<TennisGame> implements TennisGame {

    public StandardGame(Supplier<ScoringStrategy<TennisGame>> strategySupplier,
                        Supplier<ScoreManager> managerSupplier) {

        super(strategySupplier, managerSupplier);
    }

    @Override
    public void processScorePoint(Player pointWinner) {
        scoringStrategy.scorePoint(this, pointWinner);

        firePropertyChange(MatchEvent.GAME_SCORES.getEventName(), null, scoreManager);
        if (isOver()) {
            firePropertyChange(MatchEvent.GAME_WINNER.getEventName(), null, winner);
        }
    }

    @Override
    protected void handleStageSpecificPropertyChange(PropertyChangeEvent event) {
        // Low-level stages do not need to listen to child stage events
    }

    @Override
    public Optional<Stage> getChildStage() {
        // Low-level stages do not have a child stage
        return Optional.empty();
    }
}
