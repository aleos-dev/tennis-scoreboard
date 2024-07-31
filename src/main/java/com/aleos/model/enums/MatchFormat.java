package com.aleos.model.enums;

import lombok.Getter;

@Getter
public enum MatchFormat {
    BO3(2), BO5(3);

    private final int setsRequiredToWin;

    MatchFormat(int setsRequiredToWin) {
        this.setsRequiredToWin = setsRequiredToWin;
    }
}
