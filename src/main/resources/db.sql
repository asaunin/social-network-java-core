CREATE TABLE users
(
    id BIGINT PRIMARY KEY NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    birth_date DATE,
    reg_date DATE,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15)
);
CREATE UNIQUE INDEX users_email_uindex ON users (email);