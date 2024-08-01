package com.aleos.match.enums;

public enum Player {
    ONE,
    TWO;

    public Player getOpponent() {
        return this == ONE ? TWO : ONE;
    }
}
