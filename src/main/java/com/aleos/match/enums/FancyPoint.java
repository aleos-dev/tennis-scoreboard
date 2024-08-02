package com.aleos.match.enums;

public enum FancyPoint {

    LOVE("LOVE"),
    ONE("I"),
    TWO("II"),
    THREE("III"),
    FOUR("IV"),
    FIVE("V"),
    SIX("VI"),
    SEVEN("VII"),
    EIGHT("VIII"),
    NINE("IX"),
    TEN("X"),
    ELEVEN("WHO_IS_THE_BOSS");

    private final String value;

    FancyPoint(String value) {
        this.value = value;
    }

    public FancyPoint increase() {
        return values()[ordinal() + 1];
    }

    @Override
    public String toString() {
        return value;
    }
}
