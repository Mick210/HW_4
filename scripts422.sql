CREATE TABLE auto
(
    car_id INT PRIMARY KEY,
    brand  VARCHAR,
    model  VARCHAR,
    cost   INTEGER
);

CREATE TABLE people
(
    person_id   INT PRIMARY KEY,
    name        VARCHAR,
    age         INT,
    has_license BOOLEAN
);

CREATE TABLE ownership
(
    ownership_id INT PRIMARY KEY,
    person_id    INT,
    car_id       INT UNIQUE,
    FOREIGN KEY (person_id) REFERENCES People (person_id),
    FOREIGN KEY (car_id) REFERENCES auto (car_id)
);

