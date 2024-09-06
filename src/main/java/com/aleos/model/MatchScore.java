package com.aleos.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class MatchScore {

    private final UUID matchID;

    private final String playerOneName;

    private final String playerTwoName;

    private final List<String> historyEntries = new ArrayList<>();

    private final Deque<String> notifications = new LinkedList<>();

    private final StringBuffer scoreSnapshot = new StringBuffer();

    @Setter
    private String[] scorePoints;

    @Setter
    private String[] scoreGames;

    @Setter
    private String[] scoreSets;

    public MatchScore(UUID matchID, String playerOneName, String playerTwoName) {
        this.matchID = matchID;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
    }

    public void addNotification(String message) {
        notifications.offer(message);
        historyEntries.add(message);
    }
}
