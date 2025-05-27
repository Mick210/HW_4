-- liquibase formatted sql

--changeset msidorenko:1
CREATE INDEX student_name_index ON student (name);

--changeset msidorenko:2
CREATE INDEX idx_faculty_search ON faculty (name, color);