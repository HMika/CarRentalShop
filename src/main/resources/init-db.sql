\c carrent_db;

DROP TABLE IF EXISTS Rentals CASCADE;
DROP TABLE IF EXISTS Cars CASCADE;
DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Roles CASCADE;

CREATE TABLE Roles
(
    RoleID   BIGSERIAL PRIMARY KEY,
    RoleName VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Users
(
    UserID      BIGSERIAL PRIMARY KEY,
    Username    VARCHAR(50)  NOT NULL UNIQUE,
    Name        VARCHAR(50)  NOT NULL,
    Surname     VARCHAR(50)  NOT NULL,
    Password    VARCHAR(255) NOT NULL,
    ContactInfo VARCHAR(100) NOT NULL UNIQUE,
    RoleID      BIGINT       NOT NULL,
    FOREIGN KEY (RoleID) REFERENCES Roles (RoleID) ON DELETE CASCADE
);

CREATE TABLE Cars
(
    CarID              BIGSERIAL PRIMARY KEY,
    Make               VARCHAR(50)        NOT NULL,
    Model              VARCHAR(50)        NOT NULL,
    Year               INT                NOT NULL,
    RegistrationNumber VARCHAR(20) UNIQUE NOT NULL,
    RentalPrice        NUMERIC(10, 2)     NOT NULL
);

CREATE TABLE Rentals
(
    RentalID   BIGSERIAL PRIMARY KEY,
    UserID     BIGINT         NOT NULL,
    CarID      BIGINT         NOT NULL,
    StartDate  DATE           NOT NULL,
    EndDate    DATE           NOT NULL,
    IsPaid     BOOLEAN        NOT NULL DEFAULT FALSE,
    TotalPrice NUMERIC(10, 2) NOT NULL DEFAULT 0.00, -- New Column Added
    FOREIGN KEY (UserID) REFERENCES Users (UserID) ON DELETE CASCADE,
    FOREIGN KEY (CarID) REFERENCES Cars (CarID) ON DELETE CASCADE
);

INSERT INTO Roles (RoleName)
VALUES ('User'),
       ('Admin');

INSERT INTO Users (Username, Name, Surname, Password, ContactInfo, RoleID)
VALUES ('john_doe', 'John', 'Doe', '$2a$10$Sq.qqT1jPy4x/RwcvEVl1e02TOprldvMs0uRnmokxsTmhZb79wBRa',
        'john.doe@example.com', 1),
       ('jane_smith', 'Jane', 'Smith', '$2a$10$fnXiL8QOfq0N9TnCAR7PZO9V1Yhtp81gqTu7RdWBIAZyRQLC6Dj/m',
        'jane.smith@example.com', 1),
       ('admin_user', 'Admin', 'User', '$2a$10$hMz7zAe4dUaxK1klh6lveumdr5KSDploE/.qvN78.4qaAXrbBL5u2',
        'admin@example.com', 2),
       ('michael_b', 'Michael', 'Brown', '$2a$10$F7LQV4WLB6n0opfpjy6gZeAlAO8U9trQjx.DHs96FgS8MAFYFB0Ou',
        'michael.b@example.com', 1),
       ('sarah_w', 'Sarah', 'White', '$2a$10$7DhDmjJAk.1.i70e.DpM3uyQ.sLhZn/9dFEjzha4AVg68gz7UbHh2',
        'sarah.w@example.com', 1);

INSERT INTO Cars (Make, Model, Year, RegistrationNumber, RentalPrice)
VALUES ('Toyota', 'Corolla', 2020, 'ABC123', 50.00),
       ('Honda', 'Civic', 2019, 'DEF456', 55.00),
       ('Ford', 'Focus', 2021, 'GHI789', 60.00),
       ('BMW', 'X5', 2022, 'JKL012', 120.00),
       ('Tesla', 'Model 3', 2023, 'MNO345', 90.00);

INSERT INTO Rentals (UserID, CarID, StartDate, EndDate, IsPaid, TotalPrice)
VALUES (1, 1, '2024-02-01', '2024-02-05', TRUE, 200.00),  -- 4 days × $50.00
       (2, 3, '2024-02-10', '2024-02-15', FALSE, 300.00), -- 5 days × $60.00
       (3, 4, '2024-03-01', '2024-03-07', TRUE, 720.00),  -- 6 days × $120.00
       (4, 2, '2024-03-05', '2024-03-10', FALSE, 275.00), -- 5 days × $55.00
       (5, 5, '2024-03-15', '2024-03-20', TRUE, 450.00); -- 5 days × $90.00
