CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password_salt VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);
