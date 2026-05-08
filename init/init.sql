USE users;

-- Create a profiles table
CREATE TABLE profiles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    userId VARCHAR(50) UNIQUE,
    name VARCHAR(255),
    age INT NOT NULL DEFAULT 0,
    hobby VARCHAR(255)
);

-- Add an index on the userId column for faster lookups
CREATE UNIQUE INDEX idx_unique_userId ON profiles(userId);