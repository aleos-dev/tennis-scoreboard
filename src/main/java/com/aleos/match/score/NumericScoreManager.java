package com.aleos.match.score;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;


public class NumericScoreManager implements Score<Integer> {

    private final List<Integer> scores = new ArrayList<>();

    public void setScore(@NonNull Integer score) {
        scores.add(score);
    }

    public Integer getScore() {
        return scores.size();
    }

    public List<Integer> getWonGameNumbers() {
        return List.copyOf(scores);
    }
}
