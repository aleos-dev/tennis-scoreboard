package com.aleos.service;

import com.aleos.match.model.enums.MatchEvent;
import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StarPresentation;
import com.aleos.match.scoremanager.ScoreManager;
import com.aleos.match.stage.TennisMatch;
import com.aleos.model.MatchScore;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreTrackerService implements PropertyChangeListener {

    private final Map<UUID, MatchScore> scores = new ConcurrentHashMap<>();

    public Optional<MatchScore> findById(UUID id) {
        return Optional.of(scores.get(id));
    }

    public void trackMatch(TennisMatch match) {
        match.addPropertyChangeListener(new WeakReference<>(this).get());
        scores.put(match.getId(), new MatchScore(match.getId(), match.getPlayerOneName(), match.getPlayerTwoName()));
    }


    public void untrackMatch(UUID id) {
        scores.remove(id);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Optional<MatchEvent> eventOpt = MatchEvent.fromEventName(evt.getPropertyName());

        eventOpt.ifPresent(matchEvent -> {
            if (isScoreEvent(matchEvent)) {
                handleScoreEvent(evt, matchEvent);
            } else if (isWinnerEvent(matchEvent)) {
                handleWinnerEvent(evt, matchEvent);
            }
        });
    }

    private boolean isScoreEvent(MatchEvent event) {
        return event == MatchEvent.GAME_SCORES || event == MatchEvent.SET_SCORES || event == MatchEvent.MATCH_SCORES;
    }

    private boolean isWinnerEvent(MatchEvent event) {
        return event == MatchEvent.GAME_WINNER || event == MatchEvent.SET_WINNER || event == MatchEvent.MATCH_WINNER;
    }

    private void handleScoreEvent(PropertyChangeEvent evt, MatchEvent event) {
        String[] newRecord = getScoreManager(evt).getScoresPresentation(StarPresentation::translateScores);
        UUID matchId = getMatchId(evt);
        MatchScore matchScore = scores.get(matchId);

        switch (event) {
            case GAME_SCORES -> matchScore.setScorePoints(newRecord);
            case SET_SCORES -> matchScore.setScoreGames(newRecord);
            case MATCH_SCORES -> matchScore.setScoreSets(newRecord);
            default -> throw new IllegalStateException("Unexpected value: " + event);
        }
    }

    private void handleWinnerEvent(PropertyChangeEvent evt, MatchEvent event) {

        String wonStage = switch (event) {
            case GAME_WINNER -> "the game!";
            case SET_WINNER -> "the set!";
            case MATCH_WINNER -> "the match!";
            default -> throw new IllegalStateException("Unexpected event: " + event);
        };

        MatchScore matchScore = scores.get(getMatchId(evt));
        Player player = (Player) evt.getNewValue();
        String playerName = player == Player.ONE ? matchScore.getPlayerOneName() : matchScore.getPlayerTwoName();
        matchScore.addNotification("%s wins %s".formatted(playerName, wonStage));
    }

    private ScoreManager getScoreManager(PropertyChangeEvent evt) {
        return (ScoreManager) evt.getNewValue();
    }

    private UUID getMatchId(PropertyChangeEvent evt) {
        return ((TennisMatch) evt.getSource()).getId();
    }
}
