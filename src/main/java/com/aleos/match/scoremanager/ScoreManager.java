package com.aleos.match.scoremanager;

import com.aleos.match.model.ScoreRecord;
import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.model.enums.StageType;

import java.util.List;
import java.util.function.BiFunction;

public interface ScoreManager {

    void setScore(ScoreRecord scoreRecord);

    int getScore(Player player);

    void awardScore(StageState state, Player player);

    String[] getScoresPresentation(BiFunction<StageType, List<ScoreRecord>, String[]> converter);
}
