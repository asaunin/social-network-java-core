CREATE TABLE users
(
  id BIGINT PRIMARY KEY NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255),
  birth_date DATE,
  reg_date TIMESTAMP DEFAULT now() NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(255),
  sex CHAR
);

CREATE SEQUENCE users_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);

CREATE UNIQUE INDEX users_email_uindex ON users (email);

CREATE TABLE friends (
  userid bigint NOT NULL,
  friendid bigint NOT NULL
);

CREATE SEQUENCE friends_friend_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE SEQUENCE friends_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

ALTER TABLE ONLY friends ALTER COLUMN friendid SET DEFAULT nextval('friends_friend_seq'::regclass);

ALTER TABLE ONLY friends ALTER COLUMN userid SET DEFAULT nextval('friends_id_seq'::regclass);

ALTER TABLE ONLY friends ADD CONSTRAINT friends_users_userid_fk FOREIGN KEY (userid) REFERENCES users(id);

ALTER TABLE ONLY friends ADD CONSTRAINT friends_users_friendid_fk FOREIGN KEY (friendid) REFERENCES users(id);

CREATE TABLE messages (
  id bigint NOT NULL,
  date timestamp without time zone DEFAULT now(),
  sender bigint NOT NULL,
  recipient bigint NOT NULL,
  body character varying(255) NOT NULL
);

CREATE SEQUENCE messages_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE SEQUENCE messages_recipient_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE SEQUENCE messages_sender_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

ALTER TABLE ONLY messages ALTER COLUMN id SET DEFAULT nextval('messages_id_seq'::regclass);

ALTER TABLE ONLY messages ALTER COLUMN sender SET DEFAULT nextval('messages_sender_seq'::regclass);

ALTER TABLE ONLY messages ALTER COLUMN recipient SET DEFAULT nextval('messages_recipient_seq'::regclass);

ALTER TABLE ONLY messages ADD CONSTRAINT messages_id_pk PRIMARY KEY (id);

ALTER TABLE ONLY messages ADD CONSTRAINT messages_recipient_id_fk FOREIGN KEY (recipient) REFERENCES users(id);

ALTER TABLE ONLY messages ADD CONSTRAINT messages_sender_id_fk FOREIGN KEY (sender) REFERENCES users(id);

CREATE INDEX fki_messages_sender_id_fk ON messages (sender);

CREATE UNIQUE INDEX messages_id_uindex ON messages (id);
