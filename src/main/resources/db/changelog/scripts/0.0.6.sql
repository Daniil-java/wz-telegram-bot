--liquibase formatted sql

--changeset DanielK:5

alter table orders
    add url text,
    add created timestamp,
    add updated timestamp,
    add is_matching_filter boolean DEFAULT FALSE;

alter table users
    add filter text;