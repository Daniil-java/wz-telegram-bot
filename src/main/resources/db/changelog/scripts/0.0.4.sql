--liquibase formatted sql

--changeset DanielK:4

alter table users
    add info text;