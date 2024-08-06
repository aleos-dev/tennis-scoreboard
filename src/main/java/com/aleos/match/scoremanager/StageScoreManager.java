package com.aleos.match.scoremanager;

import com.aleos.match.model.ScoreRecord;
import com.aleos.match.model.enums.Player;
import com.aleos.match.model.enums.StageState;
import com.aleos.match.model.enums.StageType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Getter
public class StageScoreManager implements ScoreManager {

    private final StageType stageType;

    private final List<ScoreRecord> scores = new ArrayList<>();

    public StageScoreManager(StageType stageType) {
        this.stageType = stageType;
        initializeScores();
    }

    private void initializeScores() {
        scores.add(new ScoreRecord(StageState.NOT_STARTED, 0, 0));
    }

    @Override
    public void setScore(ScoreRecord scoreRecord) {
        scores.add(scoreRecord);
    }

    @Override
    public int getScore(Player player) {
        return getLastRecord(player);
    }

    @Override
    public void awardScore(StageState state, Player player) {
        ScoreRecord lastRecord = getLastRecord();

        if (player == Player.ONE) {
            scores.add(ScoreRecord.of(state, lastRecord.p1() + 1, lastRecord.p2()));
        } else {
            scores.add(ScoreRecord.of(state, lastRecord.p1(), lastRecord.p2() + 1));
        }
    }

    @Override
    public String[] getScoresPresentation(BiFunction<StageType, List<ScoreRecord>, String[]> scoreFormatter) {
        return scoreFormatter.apply(stageType, scores);
    }

    private ScoreRecord getLastRecord() {
        return scores.getLast();
    }

    private int getLastRecord(Player player) {
        ScoreRecord last = scores.getLast();
        return player == Player.ONE ? last.p1() : last.p2();
    }
}
