-- Run: createdb moviedb   then   psql -d moviedb -f sql/schema.sql
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS movies;

CREATE TABLE movies (
    id              SERIAL PRIMARY KEY,
    title           VARCHAR(150) NOT NULL,
    genre           VARCHAR(50),
    show_time       VARCHAR(50)  NOT NULL,
    price           NUMERIC(8,2) NOT NULL,
    available_seats INT NOT NULL CHECK (available_seats >= 0)
);

CREATE TABLE bookings (
    id            SERIAL PRIMARY KEY,
    movie_id      INT NOT NULL REFERENCES movies(id),
    customer_name VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL,
    seats         INT NOT NULL CHECK (seats > 0),
    total_amount  NUMERIC(10,2) NOT NULL,
    booked_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO movies (title, genre, show_time, price, available_seats) VALUES
('Leo',             'Action',  '2026-10-10 18:30', 250.00, 50),
('Jailer',          'Action',  '2026-10-10 21:30', 220.00, 40),
('Vikram',          'Thriller','2026-10-11 16:00', 200.00, 60),
('Interstellar',    'Sci-Fi',  '2026-10-11 19:00', 300.00, 30);
