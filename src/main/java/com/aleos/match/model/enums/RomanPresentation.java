package com.aleos.match.model.enums;

public enum RomanPresentation {

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
    ELEVEN("XI");

    private static final RomanPresentation[] VALUES = values();

    private final String presentation;

    RomanPresentation(String presentation) {
        this.presentation = presentation;
    }

    public static String convert(int value) {
        return value >= 0 && value < VALUES.length
                ? VALUES[value].presentation
                : String.valueOf(value);
    }
}
