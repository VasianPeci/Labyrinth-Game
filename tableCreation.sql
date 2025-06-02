CREATE TABLE players (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL,
    row_position INT,
    col_position INT,
    row_start INT,
    col_start INT,
    row_end INT,
    col_end INT,
    points INT,
    labyrinthMatrix JSON,
    pointsArray JSON,
    arrayCount INT
);
