--liquibase formatted sql

--changeset DanielK:1

CREATE TABLE IF NOT EXISTS users
(
    id              serial PRIMARY KEY,
    telegram_id     bigint NOT NULL UNIQUE,
    chat_id         bigint NOT NULL UNIQUE,
    username        text,
    firstname       text,
    lastname        text,
    language_code   text,
    created         timestamp
);

CREATE INDEX telegram_id ON users(telegram_id);

CREATE TABLE IF NOT EXISTS user_headers
(
    id              serial PRIMARY KEY,
    cookies         text,
    agent_id        text,
    header_status   text default 'OK',
    user_id         bigint NOT NULL,
    load_attempt_count int default 0,
    created         timestamp,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    id              serial PRIMARY KEY,
    wz_id           bigint,
    category_id     bigint NOT NULL,
    subject         text,
    customer_id     bigint NOT NULL,
    description     text,
    duration        bigint,
    archived        boolean NOT NULL,
    chat_closed     boolean NOT NULL,
    price           bigint,
    status          bigint
);

