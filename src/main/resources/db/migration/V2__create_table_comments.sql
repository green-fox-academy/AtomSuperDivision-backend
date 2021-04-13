CREATE TABLE comments
(
    id         bigint not null auto_increment,
    message    text,
    created_at timestamp,
    user_id    varchar(30),
    meme_id    bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (username),
    FOREIGN KEY (meme_id) REFERENCES memes (id)
);