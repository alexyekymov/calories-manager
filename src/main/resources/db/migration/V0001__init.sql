CREATE TABLE users
(
    id               INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 10000),
    name             VARCHAR                 NOT NULL,
    email            VARCHAR                 NOT NULL,
    password         VARCHAR                 NOT NULL,
    registered       TIMESTAMP DEFAULT now() NOT NULL,
    enabled          BOOLEAN   DEFAULT TRUE  NOT NULL,
    calories_per_day INT       DEFAULT 2000  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_role
(
    user_id INT     NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_role_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE meal
(
    id          INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 10000),
    date_time   TIMESTAMP    NOT NULL,
    description VARCHAR(128) NOT NULL,
    calories    INT          NOT NULL,
    user_id     INT          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX meal_unique_datetime_idx ON meal (user_id, date_time);
