package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum Query {
    ADD_FRIEND("INSERT INTO adding_friends(outgoing_request_user_id, " +
            "incoming_request_user_id, confirmation) VALUES (?, ?, false)"),
    ADD_LIKE("INSERT INTO films_like(film_id, user_id) VALUES (?, ?)"),
    CHECKING_AVAILABILITY_FILM("SELECT COUNT(user_id) " +
            "FROM films_like " +
            "WHERE film_id = ? AND user_id = ?"),
    CHECKING_AVAILABILITY_USER("SELECT COUNT(outgoing_request_user_id) " +
            "FROM adding_friends " +
            "WHERE outgoing_request_user_id = ? AND incoming_request_user_id = ?"),
    CHECK_DIRECTOR("SELECT COUNT(id) " +
            "FROM directors " +
            "WHERE id = ?"),
    CHECK_GENRE("SELECT COUNT(id) " +
            "FROM genre " +
            "WHERE id = ?"),
    CHECK_MPA("SELECT COUNT(id) " +
            "FROM mpa " +
            "WHERE id = ?"),
    CHECK_USER("SELECT COUNT(id) " +
            "FROM users " +
            "WHERE id = ?"),
    DELETE_DIRECTOR("DELETE FROM directors " +
            "WHERE id = ?"),
    DELETE_FRIEND("DELETE FROM adding_friends " +
            "WHERE outgoing_request_user_id = ? AND incoming_request_user_id = ?"),
    DELETE_LIKE("DELETE FROM films_like WHERE film_id = ? AND user_id = ?"),
    FIND_ALL_FILMS("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, m.id AS mpa_id, " +
            "m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id"),
    FIND_ALL_USERS("SELECT * FROM users"),
    FIND_FILM_BY_ID("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, m.id AS mpa_id, " +
            "m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa as m ON f.mpa_id = m.id " +
            "WHERE f.id = ?"),
    FIND_FRIEND("SELECT incoming_request_user_id AS user_id " +
            "FROM adding_friends " +
            "WHERE outgoing_request_user_id = ? " +
            "UNION " +
            "SELECT outgoing_request_user_id AS user_id " +
            "FROM adding_friends " +
            "WHERE incoming_request_user_id = ? AND confirmation = true"),
    FIND_LIST_LIKES("SELECT user_id " +
            "FROM films_like " +
            "WHERE film_id = ?"),
    FIND_POPULAR_FILMS("SELECT film_id " +
            "FROM films_like " +
            "GROUP BY film_id " +
            "ORDER BY COUNT(user_id) DESC " +
            "LIMIT ?"),
    FIND_USERS_BY_ID("SELECT * " +
            "FROM users " +
            "WHERE id = ?"),
    GET_ALL_DIRECTORS("SELECT * " +
            "FROM directors"),
    GET_ALL_GENRE("SELECT * " +
            "FROM genre"),
    GET_ALL_MPA("SELECT * " +
            "FROM mpa"),
    GET_DIRECTOR_BY_ID("SELECT * " +
            "FROM directors " +
            "WHERE id = ?"),
    GET_GENRE_BY_ID("SELECT * " +
            "FROM genre " +
            "WHERE id = ?"),
    GET_LIKES("SELECT user_id FROM films_like WHERE film_id = ?"),
    GET_MPA_BY_ID("SELECT * " +
            "FROM mpa " +
            "WHERE id = ?"),
    INSERT_DIRECTOR("INSERT INTO directors(name) VALUES (?)"),
    INSERT_FILM("INSERT INTO films(name, description, releaseDate, duration, genre, mpa_id, directors) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)"),
    INSERT_USER("INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)"),
    UPDATE_CONFIRMATION("UPDATE adding_friends " +
            "SET confirmation = true " +
            "WHERE outgoing_request_user_id = ? AND incoming_request_user_id = ?"),
    UPDATE_DIRECTOR("UPDATE directors " +
            "SET name = ?" +
            "WHERE id = ?"),
    UPDATE_FILM("UPDATE films " +
            "SET name = ?, description = ?, releaseDate = ?, duration = ?, genre = ?, mpa_id = ?, directors = ? " +
            "WHERE id = ?"),
    UPDATE_FILM_BY_DELETE_DIRECTOR("UPDATE films " +
            "SET directors = NULL " +
            "WHERE directors LIKE ?"),
    UPDATE_USER("UPDATE users " +
            "SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE id = ?"),

    DELETE_FILM("DELETE FROM films " +
            "WHERE id = ?"),
    DELETE_FILMS_LIKE("DELETE FROM films_like " +
            "WHERE film_id = ?;"),
    DELETE_USER("DELETE FROM users " +
            "WHERE id = ?"),
    DELETE_MUTUAL_FRIEND("DELETE FROM adding_friends " +
            "WHERE (outgoing_request_user_id = ? OR incoming_request_user_id = ?) AND " +
            " confirmation = TRUE"),
    GET_FILMS_BY_DIRECTOR_ID_SORT_BY_YEAR("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, " +
            "m.id AS mpa_id, m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id " +
            "WHERE f.directors LIKE ? " +
            "GROUP BY f.id " +
            "ORDER BY f.releaseDate"),
    GET_FILMS_BY_DIRECTOR_ID_SORT_BY_LIKES("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, " +
            "m.id AS mpa_id, m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id " +
            "INNER JOIN films_like AS fl ON f.id = fl.film_id " +
            "WHERE f.directors LIKE ? " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(fl.user_id) DESC"),
    FIND_POPULAR_FILMS_BY_TITLE("SELECT f.id " +
            "FROM films AS f " +
            "LEFT JOIN films_like AS fl ON f.id = fl.films_id " +
            "WHERE f.name LIKE ? " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(user_id) DESC");

    private final String query;

    Query(String query) {
        this.query = query;
    }
}