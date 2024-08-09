CREATE TABLE player (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        country VARCHAR(30) NOT NULL,
                        image_path VARCHAR(255),
                        created_at TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
                        CONSTRAINT player_name_unique UNIQUE (name)
);