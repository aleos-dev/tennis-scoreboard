package com.aleos.match.model.enums;

import com.aleos.match.exception.ScorePresentationException;
import com.aleos.match.model.ScoreRecord;

import java.util.List;

public enum StarPresentation {

    PREPARED("🟢"),
    ZERO("0️⃣"),
    FIFTEEN("1️⃣5️⃣"),
    THIRTY("3️⃣0️⃣"),
    FORTY("4️⃣0️⃣"),
    ADVANTAGE("⚡️"),
    GAME_WINNER("🎾"),
    SET_WINNER("🌟"),
    MATCH_WINNER("🏆"),
    TIE_BREAK("🔥"),
    DEUCE("❤️"),
    LOSER("-"),
    DISADVANTAGE("_");

    private final String symbol;

    StarPresentation(String symbol) {
        this.symbol = symbol;
    }

    public static String[] translateScores(StageType stageType, List<ScoreRecord> scoreRecord) {
        return switch (stageType) {
            case GAME -> translateGameScores(scoreRecord.getLast());
            case MATCH -> translateMatchScore(scoreRecord.getLast());
            case SET -> translateSetScores(scoreRecord);
        };
    }

    private static String[] translateGameScores(ScoreRecord scoreRecord) {
        var state = scoreRecord.state();
        int score1 = scoreRecord.p1();
        int score2 = scoreRecord.p2();

        return switch (state) {
            case NOT_STARTED -> createScoreArray(PREPARED.symbol, PREPARED.symbol);

            case ONGOING -> handleStandardCase(score1, score2);

            case TIE_BREAK -> createScoreArray(TIE_BREAK.symbol + score1, TIE_BREAK.symbol + score2);

            case DEUCE -> handleDeuceCase(score1, score2);

            case FINISHED -> handleFinishedCase(GAME_WINNER.symbol, score1, score2);
        };
    }

    private static String[] handleStandardCase(int score1, int score2) {
        if (score1 == 4) {
            return createScoreArray(translateGameScore(score1), DISADVANTAGE.symbol);
        }
        if (score2 == 4) {
            return createScoreArray(DISADVANTAGE.symbol, translateGameScore(score2));
        }
        return createScoreArray(translateGameScore(score1), translateGameScore(score2));
    }

    private static String[] translateSetScores(List<ScoreRecord> records) {
        int n = records.size();

        ScoreRecord lastRecord = records.get(n - 1);
        ScoreRecord previousRecord = n > 1 ? records.get(n - 2) : null;
        StageState state = lastRecord.state();

        int score1 = lastRecord.p1();
        int score2 = lastRecord.p2();

        return switch (state) {
            case NOT_STARTED -> createScoreArray(PREPARED.symbol, PREPARED.symbol);

            case ONGOING, TIE_BREAK -> createScoreArray(String.valueOf(score1), String.valueOf(score2));

            case FINISHED -> handleSetFinishedCase(previousRecord, score1, score2);

            default -> throw new ScorePresentationException("Unknown set state: " + state);
        };
    }

    private static String[] translateMatchScore(ScoreRecord lastRecord) {
        StageState state = lastRecord.state();
        int score1 = lastRecord.p1();
        int score2 = lastRecord.p2();

        return switch (state) {
            case NOT_STARTED -> createScoreArray(PREPARED.symbol, PREPARED.symbol);

            case ONGOING -> createScoreArray(String.valueOf(score1), String.valueOf(score2));

            case FINISHED -> handleFinishedCase(MATCH_WINNER.symbol, score1, score2);

            default -> throw new ScorePresentationException("Unknown match state: " + state);
        };
    }

    private static String translateGameScore(int score) {
        return switch (score) {
            case 0 -> ZERO.symbol;
            case 1 -> FIFTEEN.symbol;
            case 2 -> THIRTY.symbol;
            case 3 -> FORTY.symbol;
            case 4 -> ADVANTAGE.symbol;
            default -> String.valueOf(score);
        };
    }

    private static String[] formatSetTieBreakScores(int score1, int score2) {
        return new String[]{
                score1 > score2 ? String.format("7(%d)%s", score1, SET_WINNER.symbol) : String.format("6(%d)", score1),
                score2 > score1 ? String.format("7(%d)%s", score2, SET_WINNER.symbol) : String.format("6(%d)", score2)
        };
    }

    private static String[] formatSetRegularScores(int score1, int score2) {
        return new String[]{
                score1 > score2 ? String.format("%d%s", score1, SET_WINNER.symbol) : String.valueOf(score1),
                score2 > score1 ? String.format("%d%s", score2, SET_WINNER.symbol) : String.valueOf(score2),
        };
    }

    private static String[] createScoreArray(String... scores) {
        return scores;
    }

    private static String[] handleDeuceCase(int score1, int score2) {
        return score1 == score2
                ? createScoreArray(DEUCE.symbol, DEUCE.symbol)
                : createScoreArray(translateGameScore(score1), translateGameScore(score2));
    }

    private static String[] handleFinishedCase(String winSymbol, int score1, int score2) {
        return createScoreArray(
                score1 > score2 ? winSymbol : LOSER.symbol,
                score2 > score1 ? winSymbol : LOSER.symbol
        );
    }

    private static String[] handleSetFinishedCase(ScoreRecord previousRecord, int score1, int score2) {
        return previousRecord != null && previousRecord.state() == StageState.TIE_BREAK
                ? formatSetTieBreakScores(score1, score2)
                : formatSetRegularScores(score1, score2);
    }
}
