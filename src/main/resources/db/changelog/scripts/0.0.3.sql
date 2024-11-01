--liquibase formatted sql

--changeset DanielK:3

ALTER TABLE orders
    ADD COLUMN notification_attempt_count INT DEFAULT 0,
    ADD COLUMN user_id BIGINT,
    ADD CONSTRAINT fk_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL;