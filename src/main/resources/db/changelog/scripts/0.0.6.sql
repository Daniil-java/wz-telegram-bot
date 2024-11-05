--liquibase formatted sql

--changeset DanielK:5

alter table orders
    add url text,
    add created timestamp,
    add updated timestamp;

alter table users
    add filter text;