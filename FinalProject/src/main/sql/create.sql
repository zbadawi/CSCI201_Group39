/*
	These are the commands to create the database schema 
    Run this script on your computer once to set it up.
*/

CREATE SCHEMA BirdsAndBees;

CREATE TABLE BirdsAndBees.Users(
	userID int PRIMARY KEY AUTO_INCREMENT,
    username varchar(45) UNIQUE NOT NULL,
    password varchar(45) NOT NULL,
    accountType int NOT NULL, # 0 for buyers, 1 for vendors
    currentBalance int,
    currentProfit int,
    productsPurchased int
);

CREATE TABLE BirdsAndBees.Products(
	productID int PRIMARY KEY AUTO_INCREMENT,
    name varchar(45) NOT NULL,
    price double NOT NULL,
    vendorID int NOT NULL, # foreign key, references Users.userID
    image text,
    quantityAvailable int,
    FOREIGN KEY (vendorID) REFERENCES Users(userID)
);

CREATE TABLE BirdsAndBees.Carts(
	userID int NOT NULL,
    productID int NOT NULL,
    quantity int NOT NULL,
    purchased boolean,
    FOREIGN KEY (userID) REFERENCES Users(userID),
    FOREIGN KEY (productID) REFERENCES Products(productID)
);







