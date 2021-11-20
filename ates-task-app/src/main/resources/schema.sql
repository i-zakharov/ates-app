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

DROP TABLE IF EXISTS Task;

CREATE TABLE Task
(
    id        IDENTITY PRIMARY KEY,
    PUBLIC_ID VARCHAR(50)  NOT NULL UNIQUE,
    title  VARCHAR(250) NOT NULL,
    description  VARCHAR(5000),
    status  VARCHAR(250) NOT NULL,
    assignee_id BIGINT  NOT NULL,
    price DECIMAL(20, 2),
    version   INT  NOT NULL,
    foreign key (assignee_id) references app_user(id)
);
