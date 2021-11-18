DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user
(
    id        IDENTITY PRIMARY KEY,
    PUBLIC_ID VARCHAR(50)  NOT NULL UNIQUE,
    username  VARCHAR(250),
    full_name  VARCHAR(250),
    email  VARCHAR(250),
    is_active BOOLEAN,
    role      VARCHAR(250),
    version   INT,
    role_version INT
);