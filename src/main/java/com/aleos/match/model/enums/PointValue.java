package com.aleos.match.model.enums;

public enum PointValue {
    ZERO,
    FIFTEEN,
    THIRTY,
    FORTY,
    ADVANTAGE;

    public PointValue increase() {
        return PointValue.values()[this.ordinal() + 1];
    }
}
