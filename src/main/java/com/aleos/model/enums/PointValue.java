package com.aleos.model.enums;

public enum PointValue {
    ZERO,
    FIFTEEN,
    THIRTY,
    FORTY,
    ADVANTAGE,
    WIN;

    public PointValue increase() {
        return PointValue.values()[this.ordinal() + 1];
    }
}
