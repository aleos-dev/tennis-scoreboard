package com.aleos.match;

import com.aleos.match.enums.MatchFormat;
import com.aleos.match.enums.Player;
import com.aleos.match.factory.Factory;
import com.aleos.match.interfaces.ScoringStrategy;
import com.aleos.match.interfaces.stage.Match;
import com.aleos.match.score.NumericScoreManager;
import com.aleos.match.strategy.StandardMatchScoringStrategy;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.beans.PropertyChangeEvent;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class StandardMatch extends AbstractStage<NumericScoreManager, Match<NumericScoreManager>> {

    private final Factory<StandardSet> setFactory;


    private final UUID id = UUID.randomUUID();

    private final MatchFormat matchFormat;

    private StandardSet currentSet;

    public StandardMatch(@NonNull ScoringStrategy<Match<NumericScoreManager>> strategy,
                         @NonNull Factory<StandardSet> setFactory) {
        super(strategy);
        this.setFactory = setFactory;
        this.matchFormat = strategy.getClass().isAssignableFrom(StandardMatchScoringStrategy.Bo3.class)
                ? MatchFormat.BO3
                : MatchFormat.BO5;
    }

    @Override
    public void scorePoint(@NonNull Player pointWinner) {
        if (currentSet == null || currentSet.isOver()) {
            currentSet = setFactory.create();
            currentSet.addPropertyChangeListener(this);
        }
        currentSet.scorePoint(pointWinner);
    }

    @Override
    public Map<Player, NumericScoreManager> getScores() {
        return scores;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("setWinner".equals(event.getPropertyName())) {
            super.scoringStrategy.scorePoint(this, ((Player) event.getNewValue()));

            if (isOver()) {
                firePropertyChange("matchWinner", null, winner);
            } else {
                // check old value for reference (it is cloned?)
                firePropertyChange("matchScores", event.getOldValue(), scores);
            }

        } else if ("setScores".equals(event.getPropertyName())) {
            firePropertyChange("setScores", event.getOldValue(), event.getNewValue());

        } else if ("gameScores".equals(event.getPropertyName())) {
            firePropertyChange("gameScores", event.getOldValue(), event.getNewValue());
        }
    }

    protected void initScores() {
        scores.put(Player.ONE, new NumericScoreManager());
        scores.put(Player.TWO, new NumericScoreManager());
    }
}
