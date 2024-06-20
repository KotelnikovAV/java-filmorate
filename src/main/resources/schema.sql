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

CREATE TABLE IF NOT EXISTS films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    description varchar(200),
    releaseDate date NOT NULL,
    duration INTEGER NOT NULL,
    genre varchar NOT NULL,
    mpa_id INTEGER REFERENCES mpa (id),
    CONSTRAINT duration_positive CHECK (duration > 0)
);

CREATE TABLE IF NOT EXISTS films_like (
    film_id INTEGER REFERENCES films (id),
    user_id INTEGER REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS adding_friends (
    outgoing_request_user_id INTEGER REFERENCES users (id),
    incoming_request_user_id INTEGER REFERENCES users (id),
    confirmation BIT
);

CREATE TABLE IF NOT EXISTS genre (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL
);