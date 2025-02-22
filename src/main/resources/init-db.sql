\c carrent_db;

-- ==================================================
-- 1. DROP TABLES (in reverse dependency order)
-- ==================================================
DROP TABLE IF EXISTS Rentals;
DROP TABLE IF EXISTS Cars;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Roles;

-- ==================================================
-- 2. CREATE TABLES (in correct dependency order)
-- ==================================================

-- 2.1 Create Roles
CREATE TABLE Roles
(
    RoleID   INT         PRIMARY KEY,
    RoleName VARCHAR(50) NOT NULL
);

-- 2.2 Create Users
CREATE TABLE Users
(
    UserID      INT           PRIMARY KEY,
    Username    VARCHAR(50)   NOT NULL,
    Name        VARCHAR(50),
    Surname     VARCHAR(50),
    Password    VARCHAR(255)  NOT NULL,
    ContactInfo VARCHAR(100),
    RoleID      INT           NOT NULL,
    FOREIGN KEY (RoleID) REFERENCES Roles (RoleID)
);

-- 2.3 Create Cars
CREATE TABLE Cars
(
    CarID              INT          PRIMARY KEY,
    Make               VARCHAR(50),
    Model              VARCHAR(50),
    Year               INT,
    RegistrationNumber VARCHAR(20),
    RentalPrice        DECIMAL(10, 2)
);

-- 2.4 Create Rentals
CREATE TABLE Rentals
(
    RentalID  INT      PRIMARY KEY,
    UserID    INT      NOT NULL,
    CarID     INT      NOT NULL,
    StartDate DATE,
    EndDate   DATE,
    IsPaid    BOOLEAN,
    FOREIGN KEY (UserID) REFERENCES Users (UserID),
    FOREIGN KEY (CarID)  REFERENCES Cars  (CarID)
);

-- ==================================================
-- 3. INSERT DATA
-- ==================================================

-- 3.1 Insert into Roles
INSERT INTO Roles (RoleID, RoleName)
VALUES
    (1, 'User'),
    (2, 'Admin');

-- 3.2 Insert into Users
INSERT INTO Users (UserID, Username, Name, Surname, Password, ContactInfo, RoleID)
VALUES
    (1, 'john_doe',    'John',    'Doe',    'password123', 'john.doe@example.com', 1),
    (2, 'jane_smith',  'Jane',    'Smith',  'securepass',  'jane.smith@example.com', 1),
    (3, 'admin_user',  'Admin',   'User',   'adminpass',   'admin@example.com', 2),
    (4, 'michael_b',   'Michael', 'Brown',  'michaelpass', 'michael.b@example.com', 1),
    (5, 'sarah_w',     'Sarah',   'White',  'sarahpass',   'sarah.w@example.com', 1);

-- 3.3 Insert into Cars
INSERT INTO Cars (CarID, Make, Model, Year, RegistrationNumber, RentalPrice)
VALUES
    (1, 'Toyota', 'Corolla', 2020, 'ABC123', 50.00),
    (2, 'Honda',  'Civic',   2019, 'DEF456', 55.00),
    (3, 'Ford',   'Focus',   2021, 'GHI789', 60.00),
    (4, 'BMW',    'X5',      2022, 'JKL012', 120.00),
    (5, 'Tesla',  'Model 3', 2023, 'MNO345', 90.00);

-- 3.4 Insert into Rentals
INSERT INTO Rentals (RentalID, UserID, CarID, StartDate, EndDate, IsPaid)
VALUES
    (1, 1, 1, '2024-02-01', '2024-02-05', TRUE),
    (2, 2, 3, '2024-02-10', '2024-02-15', FALSE),
    (3, 3, 4, '2024-03-01', '2024-03-07', TRUE),
    (4, 4, 2, '2024-03-05', '2024-03-10', FALSE),
    (5, 5, 5, '2024-03-15', '2024-03-20', TRUE);
