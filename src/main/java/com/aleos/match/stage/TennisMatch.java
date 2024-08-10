package com.aleos.match.stage;

import java.util.Optional;
import java.util.UUID;

public interface TennisMatch extends Stage {

    UUID getId();

    void scorePoint(String player);

    String getPlayerOneName();

    String getPlayerTwoName();

    void setPlayerOne(String playerOne);

    void setPlayerTwo(String playerTwo);

    Optional<String> getMatchWinner();
}
