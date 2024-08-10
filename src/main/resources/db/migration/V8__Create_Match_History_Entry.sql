CREATE TABLE match_history_entry (
                                       match_id UUID NOT NULL,
                                       entry VARCHAR(255),
                                       FOREIGN KEY (match_id) REFERENCES match(id)
);
