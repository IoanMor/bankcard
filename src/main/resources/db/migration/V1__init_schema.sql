CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled  BOOLEAN      NOT NULL
);

CREATE TABLE roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE cards
(
    id          BIGSERIAL PRIMARY KEY,
    number      VARCHAR(255)   NOT NULL UNIQUE,
    owner_id    BIGINT         NOT NULL,
    expiry_date DATE           NOT NULL,
    status      VARCHAR(20)    NOT NULL,
    balance     DECIMAL(19, 2) NOT NULL,
    CONSTRAINT fk_cards_owner
        FOREIGN KEY (owner_id) REFERENCES users (id)
);