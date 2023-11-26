/*
	These are the commands to create the database schema
    Run this script on your computer once to set it up.
    
    Make sure to include your username on the first line and password on the second line
    of a file called "sql_username_and_password" in this directory
   
     Example:
    	root
    	password
*/

CREATE SCHEMA BirdsAndBees;

CREATE TABLE BirdsAndBees.Users(
	user_id int PRIMARY KEY AUTO_INCREMENT,
    username varchar(45) UNIQUE NOT NULL,
    password varchar(45) NOT NULL,
    account_type int NOT NULL, # 0 for buyers, 1 for vendors
    current_balance int,
    current_profit int,
    products_purchased int
);

CREATE TABLE BirdsAndBees.Products(
	product_id int PRIMARY KEY AUTO_INCREMENT,
    name varchar(45) NOT NULL,
    price double NOT NULL,
    vendor_id int NOT NULL, # foreign key, references Users.userID
    image_url text,
    quantity_available int,
    FOREIGN KEY (vendor_id) REFERENCES Users(user_id)
);

CREATE TABLE BirdsAndBees.Carts(
	user_id int NOT NULL,
    product_id int NOT NULL,
    quantity int NOT NULL,
    purchased boolean,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);

# DROP SCHEMA BirdsAndBees;


