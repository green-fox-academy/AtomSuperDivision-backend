DROP TABLE IF EXISTS memes;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    username varchar(30),
    password varchar(255),
    PRIMARY KEY (username)
);

INSERT INTO users (username, password)
VALUES ('Bond', 'password');

CREATE TABLE memes
(
    id         bigint not null auto_increment,
    caption    varchar(255),
    url        varchar(255),
    funny      bigint,
    sad        bigint,
    erotic     bigint,
    scary      bigint,
    created_at bigint,
    user_id    varchar(30),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (username)
);

INSERT INTO memes (caption, url, funny, sad, erotic, scary, created_at, user_id)
VALUES ('first', 'asd', 0, 0, 0, 0, 1, 'Bond');