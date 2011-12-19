/*
 * Copyright (C) 2010 Benoit Guerout <bguerout at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.keemto.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcEventRepository implements EventRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcEventRepository.class);

    public static final String SQL_EVENTS_NEWERTHAN = "SELECT events.ts, events.message, events.providerId, events.providerUserId, " +
            "user.username, user.firstName, user.lastName, user.email " +
            "FROM events " +
            "INNER JOIN keemto_user as user ON events.username = user.username " +
            "WHERE events.ts > ?";

    public static final String SQL_MOSTRECENT_EVENT = "SELECT TOP 1 events.ts, events.message, events.providerId, events.providerUserId, " +
            "user.username, user.firstName, user.lastName, user.email " +
            "FROM events " +
            "INNER JOIN keemto_user as user ON events.username = user.username " +
            "WHERE events.username=? AND events.providerId=? ORDER BY ts DESC";

    public static final int SINCE_THE_BEGINNING = -1;

    private final JdbcTemplate jdbcTemplate;
    private final AccountLocator accountLocator;

    public JdbcEventRepository(JdbcTemplate jdbcTemplate, AccountLocator accountLocator) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountLocator = accountLocator;
    }

    @Override
    public List<Event> getAllEvents() {
        return getEvents(SINCE_THE_BEGINNING);
    }

    @Override
    public List<Event> getEvents(long newerThan) {
        return jdbcTemplate.query(SQL_EVENTS_NEWERTHAN, new Long[]{newerThan}, new EventRowMapper());
    }

    @Override
    public void persist(List<Event> events) {
        for (Event event : events) {
            persist(event);
        }
    }

    private void persist(Event event) {
        String insertEvent = "insert into events (ts,message,username,providerId,providerUserId) values(?,?,?,?,?)";
        AccountKey key = event.getAccount().getKey();
        User user = key.getUser();
        try {
            jdbcTemplate.update(insertEvent,
                    new Object[]{event.getTimestamp(), event.getMessage(), user.getUsername(), key.getProviderId(), key.getProviderUserId()});

        } catch (DuplicateKeyException e) {
            throw new DuplicateEventException("Unable to persist event " + event +
                    " because another event exists with same eventTime: " + event.getTimestamp(), e);
        }
    }

    @Override
    public Event getMostRecentEvent(Account account) {
        AccountKey key = account.getKey();
        User user = key.getUser();
        String[] parameters = {user.getUsername(), key.getProviderId()};
        try {
            return jdbcTemplate.queryForObject(SQL_MOSTRECENT_EVENT, parameters, new EventRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return createInitializationEvent(account);
        }
    }

    private Event createInitializationEvent(Account account) {
        AccountKey key = account.getKey();
        log.info("User: {} hasn't event yet for provider: {}." +
                " This is propably the first time application tried to fetch user's connections. An initialization event is returned.", key.getUser(), key.getProviderId());
        //TODO check if null object has to be created into repository or in task
        return new InitializationEvent(account);
    }

    private final class EventRowMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {

            long timestamp = rs.getLong("ts");
            String message = rs.getString("message");
            User user = new UserRowMapper().mapRow(rs, rowNum);
            AccountKey key = mapAccountKey(rs, user);
            Account account = accountLocator.findAccount(key);
            return new Event(timestamp, message, account);
        }

        private AccountKey mapAccountKey(ResultSet rs, User user) throws SQLException {
            String providerId = rs.getString("providerId");
            String providerUserId = rs.getString("providerUserId");
            return new AccountKey(providerId, providerUserId, user);
        }
    }

}
