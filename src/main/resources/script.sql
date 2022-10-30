CREATE TABLE users
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(100) UNIQUE NOT NULL,
    email  VARCHAR(255) UNIQUE NOT NULL,
    age    INTEGER,
    gender VARCHAR(1),
    height INTEGER,
    weight DECIMAL
)

CREATE TABLE activities
(
    id          SERIAL PRIMARY KEY,
    description VARCHAR(100) NOT NULL,
    duration    DECIMAL,
    calories    INTEGER,
    started     TIMESTAMP,
    user_id     SERIAL       NOT NULL references USERS (id) ON DELETE CASCADE
)

CREATE TABLE goals
(
    id          SERIAL PRIMARY KEY,
    unit        VARCHAR(50) NOT NULL,
    current     DECIMAL,
    target      DECIMAL,
    user_id     SERIAL      NOT NULL references USERS (id) ON DELETE CASCADE,
    activity_id SERIAL      NOT NULL references ACTIVITIES (id) ON DELETE CASCADE
)