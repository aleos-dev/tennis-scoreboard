package com.aleos.model.enums;

public enum Player {
    ONE,
    TWO;

    Player getOpponent() {
        return this == ONE ? TWO : ONE;
    }
}
