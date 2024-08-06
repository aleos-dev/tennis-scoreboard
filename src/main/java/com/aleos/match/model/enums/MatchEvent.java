package com.aleos.match.model.enums;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum MatchEvent {
    GAME_SCORES("gameScores"),
    GAME_WINNER("gameWinner"),
    SET_SCORES("setScores"),
    SET_WINNER("setWinner"),
    MATCH_SCORES("matchScores"),
    MATCH_WINNER("matchWinner");

    private final String eventName;

    private static final MatchEvent[] VALUES = values();

    MatchEvent(String eventName) {
        this.eventName = eventName;
    }

    public static Optional<MatchEvent> fromEventName(String eventName) {
        for (MatchEvent event : VALUES) {
            if (event.eventName.equals(eventName)) {
                return Optional.of(event);
            }
        }
        return Optional.empty();
    }
}
