-- liquibase formatted sql

-- changeset danil:v1
CREATE TABLE users (
                       id bigserial PRIMARY KEY,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       phone VARCHAR(255),
                       role VARCHAR(255) NOT NULL,
                       image VARCHAR(255)
);

-- changeset danil:v2
CREATE TABLE ad (
                    id serial PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    description TEXT,
                    image VARCHAR(255),
                    price INT NOT NULL,
                    author_id BIGINT NOT NULL,
                    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

-- changeset danil:v3
CREATE TABLE comment (
                         pk serial PRIMARY KEY,
                         created_at BIGINT,
                         text TEXT,
                         author BIGINT,
                         ad INT,
                         FOREIGN KEY (author) REFERENCES users(id) ON DELETE CASCADE,
                         FOREIGN KEY (ad) REFERENCES ad(id) ON DELETE CASCADE
);
