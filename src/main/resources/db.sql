CREATE TABLE messages
(
    id BIGINT PRIMARY KEY NOT NULL,
    date DATE DEFAULT ('now'::text)::date,
    sender BIGINT DEFAULT nextval('messages_sender_seq'::regclass) NOT NULL,
    recipient BIGINT DEFAULT nextval('messages_recipient_seq'::regclass) NOT NULL
);
CREATE TABLE users
(
    id BIGINT PRIMARY KEY NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    birth_date DATE,
    reg_date DATE DEFAULT ('now'::text)::date NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    sex CHAR
);
ALTER TABLE messages ADD FOREIGN KEY (sender) REFERENCES users (id);
ALTER TABLE messages ADD FOREIGN KEY (recipient) REFERENCES users (id);
CREATE UNIQUE INDEX messages_id_uindex ON messages (id);
CREATE UNIQUE INDEX users_email_uindex ON users (email);