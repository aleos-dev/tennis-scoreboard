CREATE TABLE match
(
    id         UUID PRIMARY KEY,
    player1_id BIGINT    NOT NULL,
    player2_id BIGINT    NOT NULL,
    winner_id  BIGINT    NOT NULL,
    format     VARCHAR   NOT NULL,
    status     VARCHAR   NOT NULL,
    history    VARCHAR   NOT NULL,
    instant    TIMESTAMP NOT NULL,
    info       VARCHAR(255),
    CONSTRAINT fk_player1 FOREIGN KEY (player1_id) REFERENCES player (id),
    CONSTRAINT fk_player2 FOREIGN KEY (player2_id) REFERENCES player (id),
    CONSTRAINT fk_winner FOREIGN KEY (winner_id) REFERENCES player (id)
);
