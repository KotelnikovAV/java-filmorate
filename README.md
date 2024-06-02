![DB filmorate](https://github.com/KotelnikovAV/java-filmorate/assets/155972005/5bc5e4b2-9579-4633-b68f-64a78c16f227)

# Описание ER-диаграммы к приложению filmorate.
## Структура базы данных к приложению filmorate.
База данных (далее БД) к приложению filmorate состоит из следующих таблиц: 
- films (таблица хранения фильмов);
- users (таблица хранения пользователей);
- genre (таблица хранения жанров фильмов);
- films_like (таблица хранения лайков фильмов);
- adding_friends (таблица хранения друзей).

## Структура таблицы adding_friends.
Таблица состоит из 4 полей:
- adding_friends_id (первичный ключ);
- outgoing_request_user_id (id пользователя, который отправил заявку на добавления в друзья);
- incoming_request_user_id (id пользователя, которому пришла заявка на добавления в друзья);
- confimation (true или false - информация о том, была ли принята заявка или нет, по умолчанию false).

### Логика работы таблицы adding_friends.
По умолчанию в поле confimation заполняется значение false. Если пользователь incoming_request_user_id принимает заяку, то в БД происходит попытка добавить новую запись, если находится запись с подходящей комбинацией (порядок неважен), то поле confimation меняется на true.

## Примеры запросов к БД.
Нахождение 10 самых популярных фильмов:  
*SELECT DISTINCT film_id  
FROM films_like  
GROUP BY film_id  
ORDER BY COUNT(like_id) DESC  
LIMIT 10;*  

Нахождение всех друзей пользователя:  
*SELECT incoming_request_user_id AS user_id  
FROM adding_friends  
WHERE outgoing_request_user_id = 4 -- id пользователя  
      AND confimation = 1  
UNION  
SELECT outgoing_request_user_id AS user_id  
FROM adding_friends  
WHERE incoming_request_user_id = 4 -- id пользователя  
      AND confimation = 1*
