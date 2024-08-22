CREATE TABLE match
(
    id                 UUID PRIMARY KEY,
    player1_id         BIGINT    NOT NULL,
    player2_id         BIGINT    NOT NULL,
    winner_id          BIGINT    NOT NULL,
    format             VARCHAR   NOT NULL,
    concluded_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    final_score_record VARCHAR   NOT NULL,
    CONSTRAINT fk_player1 FOREIGN KEY (player1_id) REFERENCES player (id),
    CONSTRAINT fk_player2 FOREIGN KEY (player2_id) REFERENCES player (id),
    CONSTRAINT fk_winner FOREIGN KEY (winner_id) REFERENCES player (id)
);
