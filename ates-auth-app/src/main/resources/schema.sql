DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user
(
    id        VARCHAR(50) PRIMARY KEY,
    username  VARCHAR(250) NOT NULL,
    password  VARCHAR(250) NOT NULL,
    is_active BOOLEAN      NOT NULL,
    roles     VARCHAR(1000)
);