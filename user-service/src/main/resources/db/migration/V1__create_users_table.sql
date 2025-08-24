-- Create enum type for user roles
CREATE TYPE user_role AS ENUM ('USER', 'AUTHOR', 'EDITOR', 'ADMIN');

-- Create users table
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    name VARCHAR(255),
    age INTEGER,
    role VARCHAR(50) DEFAULT 'USER',
    provider VARCHAR(255),
    provider_id VARCHAR(255)
);
