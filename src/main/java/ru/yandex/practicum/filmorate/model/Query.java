package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum Query {
    ADD_FRIEND("INSERT INTO adding_friends(outgoing_request_user_id, " +
            "incoming_request_user_id, confirmation) VALUES (?, ?, false)"),
    ADD_LIKE("INSERT INTO films_like(film_id, user_id) VALUES (?, ?)"),
    ADD_REVIEW("INSERT INTO reviews(content, isPositive, user_id, films_id, useful) VALUES (?, ?, ?, ?, ?)"),
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
    DELETE_FILM("DELETE FROM films " +
            "WHERE id = ?"),
    DELETE_FILMS_LIKE("DELETE FROM films_like " +
            "WHERE film_id = ?;"),
    DELETE_FRIEND("DELETE FROM adding_friends " +
            "WHERE outgoing_request_user_id = ? AND incoming_request_user_id = ?"),
    DELETE_LIKE("DELETE FROM films_like WHERE film_id = ? AND user_id = ?"),
    DELETE_LIKE_FROM_REVIEW("DELETE FROM review_user_likes WHERE review_id = ? AND user_id = ?"),
    DELETE_REVIEW("DELETE FROM reviews WHERE review_id = ?"),
    DELETE_USER("DELETE FROM users " +
            "WHERE id = ?"),
    FIND_ALL_FILMS("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, m.id AS mpa_id, " +
            "m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id"),
    FIND_ALL_USERS("SELECT * FROM users"),
    FIND_COMMON_FILMS_LIST("(SELECT fl1.film_id " +
            "FROM films_like fl1 " +
            "WHERE fl1.user_id = ? )" +
            "UNION " +
            "(SELECT fl2.film_id " +
            "FROM films_like fl2 " +
            "WHERE fl2.user_id = ?)" +
            "ORDER BY film_id DESC"),
    FIND_DIRECTOR_LIST_BY_NAME("SELECT * " +
            "FROM directors " +
            "WHERE directors.name iLIKE ? "),
    FIND_FILM_BY_ID("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, m.id AS mpa_id, " +
            "m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa as m ON f.mpa_id = m.id " +
            "WHERE f.id = ?"),
    FIND_FRIEND("SELECT * FROM users " +
            "WHERE id IN (SELECT incoming_request_user_id AS user_id " +
            "FROM adding_friends " +
            "WHERE outgoing_request_user_id = ? " +
            "UNION " +
            "SELECT outgoing_request_user_id AS user_id " +
            "FROM adding_friends " +
            "WHERE incoming_request_user_id = ? AND confirmation = true)"),
    FIND_LIST_LIKED_FILMS("SELECT film_id " +
            "FROM films_like " +
            "WHERE user_id = ?"),
    FIND_LIST_LIKES("SELECT user_id " +
            "FROM films_like " +
            "WHERE film_id = ?"),
    FIND_POPULAR_FILMS("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, m.id AS mpa_id, " +
            "m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id " +
            "LEFT JOIN (SELECT film_id, COUNT(user_id) AS likes_count FROM films_like GROUP BY film_id) AS fl ON f.id = fl.film_id " +
            "ORDER BY fl.likes_count DESC " +
            "LIMIT ?"),
    FIND_POPULAR_FILMS_SORT_BY_GENRE("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, m.id AS mpa_id, " +
            "m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id " +
            "LEFT JOIN (SELECT film_id, COUNT(user_id) AS likes_count FROM films_like GROUP BY film_id) AS fl ON f.id = fl.film_id " +
            "WHERE f.genre iLIKE ? " +
            "ORDER BY fl.likes_count DESC " +
            "LIMIT ?"),
    FIND_POPULAR_FILMS_SORT_BY_GENRE_AND_YEAR("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, m.id AS mpa_id, " +
            "m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id " +
            "LEFT JOIN (SELECT film_id, COUNT(user_id) AS likes_count FROM films_like GROUP BY film_id) AS fl ON f.id = fl.film_id " +
            "WHERE f.genre iLIKE ? AND EXTRACT(year FROM f.releaseDate) = ? " +
            "ORDER BY fl.likes_count DESC " +
            "LIMIT ?"),
    FIND_POPULAR_FILMS_SORT_BY_YEAR("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, m.id AS mpa_id, " +
            "m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id " +
            "LEFT JOIN (SELECT film_id, COUNT(user_id) AS likes_count FROM films_like GROUP BY film_id) AS fl ON f.id = fl.film_id " +
            "WHERE EXTRACT(year FROM f.releaseDate) = ? " +
            "ORDER BY fl.likes_count DESC " +
            "LIMIT ?"),
    FIND_POPULAR_FILMS_BY_TITLE("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, m.id AS mpa_id, " +
            "m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id " +
            "LEFT JOIN films_like AS fl ON f.id = fl.film_id " +
            "WHERE f.name iLIKE ? " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(fl.user_id) DESC"),
    FIND_POPULAR_FILMS_BY_TITLE_AND_DIRECTOR("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, " +
            "m.id AS mpa_id, m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id " +
            "LEFT JOIN films_like AS fl ON f.id = fl.film_id " +
            "WHERE f.name iLIKE ? OR f.directors iLIKE (SELECT CAST(directors.id AS VARCHAR(10)) " +
            "FROM directors " +
            "WHERE directors.name iLIKE ?) " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(fl.user_id) DESC"),
    FIND_USERS_BY_ID("SELECT * " +
            "FROM users " +
            "WHERE id = ?"),
    GET_ALL_DIRECTORS("SELECT * " +
            "FROM directors"),
    GET_ALL_FILMS_BY_DIRECTOR_ID_SORT_BY_LIKES("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, " +
            "m.id AS mpa_id, m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.id " +
            "LEFT JOIN films_like AS fl ON f.id = fl.film_id " +
            "WHERE f.directors iLIKE ? " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(fl.user_id) DESC"),
    GET_ALL_GENRE("SELECT * " +
            "FROM genre"),
    GET_ALL_MPA("SELECT * " +
            "FROM mpa"),
    GET_DIRECTOR_BY_ID("SELECT * " +
            "FROM directors " +
            "WHERE id = ?"),
    GET_FILMS_BY_DIRECTOR_ID_SORT_BY_LIKES("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, " +
            "m.id AS mpa_id, m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id " +
            "LEFT JOIN films_like AS fl ON f.id = fl.film_id " +
            "WHERE f.directors LIKE ? " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(fl.user_id) DESC"),
    GET_FILMS_BY_DIRECTOR_ID_SORT_BY_YEAR("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, " +
            "m.id AS mpa_id, m.name AS mpa_name, f.directors " +
            "FROM films AS f " +
            "INNER JOIN mpa AS m ON f.mpa_id = m.id " +
            "WHERE f.directors LIKE ? " +
            "GROUP BY f.id " +
            "ORDER BY f.releaseDate"),
    GET_GENRE_BY_ID("SELECT * " +
            "FROM genre " +
            "WHERE id = ?"),
    GET_LIKES("SELECT user_id FROM films_like WHERE film_id = ?"),
    GET_MPA_BY_ID("SELECT * " +
            "FROM mpa " +
            "WHERE id = ?"),
    GET_REVIEWS("SELECT * FROM reviews WHERE films_id = ? ORDER BY useful DESC LIMIT ?"),
    GET_REVIEWS_WITH_COUNT("SELECT * FROM reviews ORDER BY useful DESC LIMIT ?"),
    GET_REVIEW_WITH_ID("SELECT * FROM reviews WHERE review_id = ?"),
    GET_USERS_EVENTS("SELECT ue.* " +
            "FROM users_events_table AS uet " +
            "JOIN users_events AS ue ON uet.user_events_id = ue.id " +
            "WHERE uet.user_id = ? " +
            "ORDER BY ue.timestamp"),
    GET_USERS_ID_FROM_FILMS_LIKE("SELECT DISTINCT user_id FROM films_like"),
    INSERT_DIRECTOR("INSERT INTO directors(name) VALUES (?)"),
    INSERT_FILM("INSERT INTO films(name, description, releaseDate, duration, genre, mpa_id, directors) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)"),
    INSERT_LIKE("INSERT INTO review_user_likes (review_id, user_id, like_or_dislike) VALUES (?, ?, ?)"),
    INSERT_USER("INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)"),
    INSERT_USERS_EVENTS_TABLE("INSERT INTO users_events_table( user_id, user_events_id) VALUES (?, ?)"),
    INSERT_USER_EVENT("INSERT INTO users_events(user_id, event_type, operation, entity_id, timestamp) " +
            " VALUES (?, ?, ?, ?, ?)"),
    IS_REVIEW_LIKE_OR_DISLIKE("SELECT COUNT(*) FROM review_user_likes WHERE REVIEW_ID = ? AND USER_ID = ?"),
    LIKE_REVIEW("UPDATE reviews SET useful = useful + 1 WHERE review_id = ?"),
    LIKE_REVIEW_IF_DISLIKE("UPDATE reviews SET useful = useful + 2 WHERE review_id = ?"),
    REMOVE_LIKE("UPDATE reviews SET useful = useful - 1 WHERE review_id = ?"),
    REMOVE_LIKE_IF_DISLIKE("UPDATE reviews SET useful = useful - 2 WHERE review_id = ?"),
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

    UPDATE_REVIEW("UPDATE reviews SET content = ?, isPositive = ? WHERE review_id = ?"),
    UPDATE_USER("UPDATE users " +
            "SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE id = ?");

    private final String query;

    Query(String query) {
        this.query = query;
    }
}