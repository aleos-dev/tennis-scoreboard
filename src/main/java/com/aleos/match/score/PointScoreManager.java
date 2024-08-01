package com.aleos.match.score;

import com.aleos.match.enums.PointValue;
import lombok.NonNull;

public class PointScoreManager implements Score<PointValue> {

    PointValue score = PointValue.ZERO;

    @Override
    public void setScore(@NonNull PointValue score) {
        this.score = score;
    }

    @Override
    public PointValue getScore() {
        return score;
    }
}
