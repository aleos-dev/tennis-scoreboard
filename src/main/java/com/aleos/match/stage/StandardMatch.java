package com.aleos.match.stage;

import com.aleos.match.creation.Factory;
import com.aleos.match.model.enums.MatchEvent;
import com.aleos.match.model.enums.MatchFormat;
import com.aleos.match.model.enums.Player;
import com.aleos.match.scoremanager.ScoreManager;
import com.aleos.match.scoring.ScoringStrategy;
import com.aleos.match.scoring.strategy.TennisMatchScoringStrategy;
import lombok.Getter;
import lombok.NonNull;

import java.beans.PropertyChangeEvent;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class StandardMatch<C extends TennisSet> extends AbstractStage<TennisMatch> implements TennisMatch {

    private final Factory<C> setFactory;

    @Getter
    private final UUID id = UUID.randomUUID();

    @Getter
    private final MatchFormat matchFormat;

    private C currentSet;

    public StandardMatch(@NonNull Supplier<ScoringStrategy<TennisMatch>> strategySupplier,
                         @NonNull Supplier<ScoreManager> managerSupplier,
                         @NonNull Factory<C> setFactory) {
        super(strategySupplier, managerSupplier);
        this.setFactory = setFactory;
        this.matchFormat = scoringStrategy.getClass().isAssignableFrom(TennisMatchScoringStrategy.Bo3.class)
                ? MatchFormat.BO3
                : MatchFormat.BO5;
    }

    @Override
    public void processScorePoint(@NonNull Player pointWinner) {
        if (currentSet == null || currentSet.isOver()) {
            currentSet = setFactory.create();
            currentSet.addPropertyChangeListener(this);
        }
        currentSet.scorePoint(pointWinner);
    }

    @Override
    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    @Override
    protected void handleStageSpecificPropertyChange(PropertyChangeEvent event) {
        MatchEvent.fromEventName(event.getPropertyName())
                .filter(name -> name == MatchEvent.SET_WINNER)
                .map(name -> event)
                .ifPresent(this::handleSetStageWinner);
    }

    private void handleSetStageWinner(PropertyChangeEvent event) {
        scoringStrategy.scorePoint(this, (Player) event.getNewValue());

        firePropertyChange(MatchEvent.MATCH_SCORES.getEventName(), null, scoreManager);
        if (isOver()) {
            firePropertyChange(MatchEvent.MATCH_WINNER.getEventName(), null, winner);
        }
    }

    @Override
    public Optional<Stage> getChildStage() {
        return Optional.ofNullable(currentSet);
    }
}