package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Query;
import ru.yandex.practicum.filmorate.model.UserEvent;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserEventRepositoryImpl implements UserEventRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<UserEvent> mapper;

    @Override
    public void createUserEvent(UserEvent userEvent) {
        log.info("Отправка запроса INSERT_USER_EVENT");
        int id = BaseDbStorage.insert(
                jdbc,
                Query.INSERT_USER_EVENT.getQuery(),
                userEvent.getUserId(),
                userEvent.getEventType().name(),
                userEvent.getOperation().name(),
                userEvent.getEntityId(),
                Timestamp.from(userEvent.getTimestamp().toInstant())
        );
        userEvent.setEventId(id);

        log.info("Отправка запроса INSERT_USERS_EVENTS_TABLE");
        jdbc.update(Query.INSERT_USERS_EVENTS_TABLE.getQuery(), userEvent.getUserId(), userEvent.getEventId());
    }

    @Override
    public List<UserEvent> getAllUserEvents(int userId) {
        log.info("Отправка запроса GET_USERS_EVENTS");
        return jdbc.query(Query.GET_USERS_EVENTS.getQuery(), mapper, userId);
    }
}
