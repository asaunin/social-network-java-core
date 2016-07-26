DO
$do$
DECLARE
  _db TEXT := 'test';
  _user TEXT := 'posrgres';
  _password TEXT := 'posrgres';
BEGIN
  CREATE EXTENSION IF NOT EXISTS dblink; -- enable extension
  IF EXISTS (SELECT 1 FROM pg_database WHERE datname = _db) THEN
    RAISE NOTICE 'Database already exists';
  ELSE
    PERFORM dblink_connect('host=localhost user=' || _user || ' password=' || _password || ' dbname=' || current_database());
    PERFORM dblink_exec('CREATE DATABASE ' || _db);
  END IF;
END
$do$;
CREATE TABLE IF NOT EXISTS friends
(
    userid BIGINT DEFAULT nextval('friends_id_seq'::regclass) NOT NULL,
    friendid BIGINT DEFAULT nextval('friends_friend_seq'::regclass) NOT NULL
);
CREATE TABLE IF NOT EXISTS messages
(
    id BIGINT PRIMARY KEY NOT NULL,
    date TIMESTAMP DEFAULT now(),
    sender BIGINT DEFAULT nextval('messages_sender_seq'::regclass) NOT NULL,
    recipient BIGINT DEFAULT nextval('messages_recipient_seq'::regclass) NOT NULL,
    body VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS users
(
    id BIGINT PRIMARY KEY NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    birth_date DATE,
    reg_date TIMESTAMP DEFAULT now() NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    sex CHAR
);
ALTER TABLE IF NOT EXISTS friends ADD FOREIGN KEY (userid) REFERENCES users (id);
ALTER TABLE IF NOT EXISTS friends ADD FOREIGN KEY (friendid) REFERENCES users (id);
ALTER TABLE IF NOT EXISTS messages ADD FOREIGN KEY (sender) REFERENCES users (id);
ALTER TABLE IF NOT EXISTS messages ADD FOREIGN KEY (recipient) REFERENCES users (id);
CREATE UNIQUE INDEX IF NOT EXISTS messages_id_uindex ON messages (id);
CREATE INDEX IF NOT EXISTS fki_messages_sender_id_fk ON messages (sender);
CREATE UNIQUE INDEX IF NOT EXISTS users_email_uindex ON users (email);