package com.aleos.model;


import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class MatchScore {

    @Getter
    private final UUID matchID;

    @Getter
    private final String playerOneName;

    @Getter
    private final String playerTwoName;

    @Getter
    @Setter
    private String[] scorePoints;

    @Getter
    @Setter
    private String[] scoreGames;

    @Getter
    @Setter
    private String[] scoreSets;

    @Getter
    private final List<String> historyEntries = new ArrayList<>();

    @Getter
    private final Deque<String> notifications = new LinkedList<>();

    @Getter
    private final StringBuffer scoreSnapshot = new StringBuffer();


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
