INSERT INTO match (id, player1_id, player2_id, winner_id, format, concluded_at, final_score_record)
VALUES ('ff41b2e8-130e-4756-a35c-7ff889b07a11',
        (SELECT id FROM player WHERE name = 'Serena Williams'),
        (SELECT id FROM player WHERE name = 'Roger Federer'),
        (SELECT id FROM player WHERE name = 'Roger Federer'),
        'Best of 5', CURRENT_TIMESTAMP - INTERVAL '5' HOUR + INTERVAL '00' MINUTE, ''),

       ('2b679f56-a4b5-4a9a-99a2-28ea56e8374c',
        (SELECT id FROM player WHERE name = 'Naomi Osaka'),
        (SELECT id FROM player WHERE name = 'Rafael Nadal'),
        (SELECT id FROM player WHERE name = 'Naomi Osaka'),
        'Best of 3',
        CURRENT_TIMESTAMP - INTERVAL '1' DAY + INTERVAL '1' HOUR - INTERVAL '10' MINUTE, ''),

       ('1495f284-26f7-40cf-966a-0d4f7cd546f0',
        (SELECT id FROM player WHERE name = 'Simona Halep'),
        (SELECT id FROM player WHERE name = 'Novak Djokovic'),
        (SELECT id FROM player WHERE name = 'Novak Djokovic'),
        'Best of 3', CURRENT_TIMESTAMP - INTERVAL '2' DAY + INTERVAL '4' HOUR, ''),

       ('82ac6e9a-daf5-4ab3-9e80-bf039b483efb',
        (SELECT id FROM player WHERE name = 'Ashleigh Barty'),
        (SELECT id FROM player WHERE name = 'Dominic Thiem'),
        (SELECT id FROM player WHERE name = 'Dominic Thiem'),
        'Best of 5', CURRENT_TIMESTAMP - INTERVAL '2' DAY, ''),

       ('603df065-46b8-41ab-b1c3-8c0f455aaadc',
        (SELECT id FROM player WHERE name = 'Venus Williams'),
        (SELECT id FROM player WHERE name = 'Andy Murray'),
        (SELECT id FROM player WHERE name = 'Andy Murray'),
        'Best of 3',
        CURRENT_TIMESTAMP - INTERVAL '2' DAY - INTERVAL '12' HOUR - INTERVAL '30' MINUTE, ''),

       ('a28bcb12-56fc-4039-b121-c7df4615c05b',
        (SELECT id FROM player WHERE name = 'Maria Sharapova'),
        (SELECT id FROM player WHERE name = 'Serena Williams'),
        (SELECT id FROM player WHERE name = 'Maria Sharapova'),
        'Best of 5', CURRENT_TIMESTAMP - INTERVAL '3' DAY + INTERVAL '1' HOUR, ''),

       ('865af6fb-2370-4c21-a11e-45b68126de0a',
        (SELECT id FROM player WHERE name = 'Roger Federer'),
        (SELECT id FROM player WHERE name = 'Naomi Osaka'),
        (SELECT id FROM player WHERE name = 'Roger Federer'),
        'Best of 3',
        CURRENT_TIMESTAMP - INTERVAL '3' DAY + INTERVAL '2' HOUR + INTERVAL '15' MINUTE, ''),

       ('c7443a21-57af-4823-8f60-b1158fb6d6a3',
        (SELECT id FROM player WHERE name = 'Rafael Nadal'),
        (SELECT id FROM player WHERE name = 'Simona Halep'),
        (SELECT id FROM player WHERE name = 'Rafael Nadal'),
        'Best of 3',
        CURRENT_TIMESTAMP - INTERVAL '4' DAY - INTERVAL '7' HOUR - INTERVAL '45' MINUTE, ''),

       ('3bd226ba-c416-4e4a-86c8-ccf295d46bf6',
        (SELECT id FROM player WHERE name = 'Novak Djokovic'),
        (SELECT id FROM player WHERE name = 'Ashleigh Barty'),
        (SELECT id FROM player WHERE name = 'Novak Djokovic'),
        'Best of 3', '2024-08-13 11:00:00', ''),

       ('3299b9fa-f10b-48e9-96bd-08acfe87b811',
        (SELECT id FROM player WHERE name = 'Dominic Thiem'),
        (SELECT id FROM player WHERE name = 'Venus Williams'),
        (SELECT id FROM player WHERE name = 'Dominic Thiem'),
        'Best of 5', '2024-08-11 10:00:00', ''),

       ('c651a4df-5061-4e2a-aa0c-e5ec75e3fe6e',
        (SELECT id FROM player WHERE name = 'Andy Murray'),
        (SELECT id FROM player WHERE name = 'Maria Sharapova'),
        (SELECT id FROM player WHERE name = 'Andy Murray'),
        'Best of 5', '2024-08-10 14:00:00', '');

-- Matches from 2020
INSERT INTO match (id, player1_id, player2_id, winner_id, format, concluded_at, final_score_record)
VALUES ('d1f5d1e1-fd5c-4b0a-bb2d-1c8b45f7a3e2',
        (SELECT id FROM player WHERE name = 'Serena Williams'),
        (SELECT id FROM player WHERE name = 'Naomi Osaka'),
        (SELECT id FROM player WHERE name = 'Serena Williams'),
        'Best of 3', '2020-02-15 14:00:00', ''),

       ('d2e5f3b2-b96e-4d7a-8899-1bd4526f8e21',
        (SELECT id FROM player WHERE name = 'Roger Federer'),
        (SELECT id FROM player WHERE name = 'Rafael Nadal'),
        (SELECT id FROM player WHERE name = 'Roger Federer'),
        'Best of 5', '2020-04-10 16:00:00', ''),

       ('a7d4c9c7-56e5-497d-bad5-1d8c17f1a9d4',
        (SELECT id FROM player WHERE name = 'Simona Halep'),
        (SELECT id FROM player WHERE name = 'Ashleigh Barty'),
        (SELECT id FROM player WHERE name = 'Ashleigh Barty'),
        'Best of 3', '2020-05-20 11:00:00', '');

-- Matches from 2021
INSERT INTO match (id, player1_id, player2_id, winner_id, format, concluded_at, final_score_record)
VALUES ('c3d6a5e7-87b4-41c5-8a2d-3d4f75f1c6e3',
        (SELECT id FROM player WHERE name = 'Novak Djokovic'),
        (SELECT id FROM player WHERE name = 'Dominic Thiem'),
        (SELECT id FROM player WHERE name = 'Novak Djokovic'),
        'Best of 5', '2021-03-10 12:00:00', ''),

       ('e4f7a6d8-9e5b-437f-bc8f-4e3f25b6c5d4',
        (SELECT id FROM player WHERE name = 'Venus Williams'),
        (SELECT id FROM player WHERE name = 'Maria Sharapova'),
        (SELECT id FROM player WHERE name = 'Maria Sharapova'),
        'Best of 3', '2021-07-05 15:00:00', ''),

       ('f5a8b7c9-2d7b-48e7-b8d8-6a4e38f7e5b2',
        (SELECT id FROM player WHERE name = 'Serena Williams'),
        (SELECT id FROM player WHERE name = 'Roger Federer'),
        (SELECT id FROM player WHERE name = 'Serena Williams'),
        'Best of 3', '2021-09-12 18:00:00', '');

-- Matches from 2022
INSERT INTO match (id, player1_id, player2_id, winner_id, format, concluded_at, final_score_record)
VALUES ('a6d9b8c7-57c6-499d-9a5e-3f6a4e5b7d8a',
        (SELECT id FROM player WHERE name = 'Rafael Nadal'),
        (SELECT id FROM player WHERE name = 'Andy Murray'),
        (SELECT id FROM player WHERE name = 'Rafael Nadal'),
        'Best of 5', '2022-01-22 10:00:00', ''),

       ('b7c9d8e1-6f8b-46a5-987a-2d4e59f6b5c4',
        (SELECT id FROM player WHERE name = 'Simona Halep'),
        (SELECT id FROM player WHERE name = 'Naomi Osaka'),
        (SELECT id FROM player WHERE name = 'Simona Halep'),
        'Best of 3', '2022-06-18 14:00:00', ''),

       ('c8f9e1b2-7e9b-49a5-89a7-1d3f25c6a4b5',
        (SELECT id FROM player WHERE name = 'Ashleigh Barty'),
        (SELECT id FROM player WHERE name = 'Dominic Thiem'),
        (SELECT id FROM player WHERE name = 'Dominic Thiem'),
        'Best of 3', '2022-10-15 13:00:00', '');

-- Matches from 2023
INSERT INTO match (id, player1_id, player2_id, winner_id, format, concluded_at, final_score_record)
VALUES ('d9f2a1e7-6f5b-45c7-8b7d-3e4f17c6b9a3',
        (SELECT id FROM player WHERE name = 'Novak Djokovic'),
        (SELECT id FROM player WHERE name = 'Maria Sharapova'),
        (SELECT id FROM player WHERE name = 'Novak Djokovic'),
        'Best of 3', '2023-03-15 12:00:00', ''),

       ('e1f3a2b4-8e9b-4a6b-98a7-4f5c26d8b9a2',
        (SELECT id FROM player WHERE name = 'Roger Federer'),
        (SELECT id FROM player WHERE name = 'Rafael Nadal'),
        (SELECT id FROM player WHERE name = 'Rafael Nadal'),
        'Best of 5', '2023-05-10 17:00:00', ''),

       ('f4a7b6d9-3e8b-49c7-b9e8-2e5f48c7d5b3',
        (SELECT id FROM player WHERE name = 'Serena Williams'),
        (SELECT id FROM player WHERE name = 'Andy Murray'),
        (SELECT id FROM player WHERE name = 'Serena Williams'),
        'Best of 3', '2023-08-12 16:00:00', '');
