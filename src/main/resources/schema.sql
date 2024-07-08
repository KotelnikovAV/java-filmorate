SET REFERENTIAL_INTEGRITY TRUE;

DROP TABLE IF EXISTS films_like;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS adding_friends;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS mpa;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS directors;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar NOT NULL,
    login varchar NOT NULL,
    name varchar,
    birthday date NOT NULL,
    CONSTRAINT uq_email UNIQUE(email),
    CONSTRAINT uq_login UNIQUE(login)
);

CREATE TABLE IF NOT EXISTS mpa (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS directors (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    description varchar(200),
    releaseDate date NOT NULL,
    duration INTEGER NOT NULL,
    genre varchar NOT NULL,
    mpa_id INTEGER REFERENCES mpa (id),
    directors varchar,
    CONSTRAINT duration_positive CHECK (duration > 0)
);

CREATE TABLE IF NOT EXISTS films_like (
    film_id INTEGER,
    user_id INTEGER,
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS adding_friends (
    outgoing_request_user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    incoming_request_user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    confirmation BIT
);

CREATE TABLE IF NOT EXISTS genre (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL
);