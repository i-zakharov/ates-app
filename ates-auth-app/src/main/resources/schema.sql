DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user
(
    id        IDENTITY PRIMARY KEY,
    PUBLIC_ID VARCHAR(50)  NOT NULL UNIQUE,
    username  VARCHAR(250) NOT NULL,
    password  VARCHAR(250) NOT NULL,
    is_active BOOLEAN      NOT NULL,
    role      VARCHAR(250)
);