ALTER TABLE users
    ADD COLUMN nickname VARCHAR(255);
ALTER TABLE users
    ADD CONSTRAINT uk_nickname UNIQUE (nickname);