CREATE TABLE player (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        country VARCHAR(30) NOT NULL,
                        imagePath VARCHAR(255),
                        CONSTRAINT player_name_unique UNIQUE (name)
);