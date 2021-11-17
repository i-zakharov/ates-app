DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user
(
    id        IDENTITY PRIMARY KEY,
    PUBLIC_ID VARCHAR(50)  NOT NULL UNIQUE,
    username  VARCHAR(250) NOT NULL,
    password  VARCHAR(250) NOT NULL,
    full_name VARCHAR(250),
    email     VARCHAR(250),
    is_active BOOLEAN      NOT NULL,
    role      VARCHAR(250) NOT NULL,
    version   INT          NOT NULL
);