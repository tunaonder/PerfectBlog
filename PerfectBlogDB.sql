/*
Tables to be dropped must be listed in a logical order based on dependency.
UserVideo and UserPhoto depend on User. Therefore, they must be dropped before User.
*/
DROP TABLE IF EXISTS Comment, Post, User;

CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR (32) NOT NULL,
    password VARCHAR (32) NOT NULL,
    first_name VARCHAR (32) NOT NULL,
    last_name VARCHAR (32) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Post
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    postText VARCHAR (5096) NOT NULL,
    imageFileName VARCHAR (256) NOT NULL,
    user_id INT UNSIGNED,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Comment
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    commentText VARCHAR (2048) NOT NULL,
    user_id INT UNSIGNED,
    post_id INT UNSIGNED,
    PRIMARY KEY (id),
    FOREIGN KEY (post_id) REFERENCES Post(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);
