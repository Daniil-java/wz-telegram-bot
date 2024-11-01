--liquibase formatted sql

--changeset DanielK:2

ALTER TABLE orders
    ADD COLUMN is_development boolean DEFAULT FALSE,
    ADD COLUMN is_solvable_by_ai boolean DEFAULT FALSE,
    ADD COLUMN processing_status text;