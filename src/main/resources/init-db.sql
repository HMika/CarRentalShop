\c carrent_db;

-- Create Users Table
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE
);

-- Create Cars Table
CREATE TABLE cars (
                      id SERIAL PRIMARY KEY,
                      brand VARCHAR(100) NOT NULL,
                      model VARCHAR(100) NOT NULL,
                      year INTEGER NOT NULL
);

-- Create Rentals Table
CREATE TABLE rentals (
                         id SERIAL PRIMARY KEY,
                         user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                         car_id INTEGER REFERENCES cars(id) ON DELETE CASCADE,
                         rental_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Sample Users
INSERT INTO users (username, email) VALUES
                                        ('john_doe', 'john@example.com'),
                                        ('jane_doe', 'jane@example.com'),
                                        ('michael_smith', 'michael@example.com');

-- Insert Sample Cars
INSERT INTO cars (brand, model, year) VALUES
                                          ('Toyota', 'Corolla', 2020),
                                          ('Ford', 'Focus', 2018),
                                          ('Honda', 'Civic', 2022);

-- Insert Sample Rentals
INSERT INTO rentals (user_id, car_id, rental_date) VALUES
                                                       (1, 2, '2024-01-15 10:00:00'),
                                                       (2, 3, '2024-02-01 14:30:00'),
                                                       (3, 1, '2024-02-10 09:15:00');
