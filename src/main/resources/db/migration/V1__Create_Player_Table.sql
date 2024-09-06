CREATE TABLE player
(
    id         BIGINT PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    country    VARCHAR(30) NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT player_name_unique UNIQUE (name)
);