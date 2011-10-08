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
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcEventRepository implements EventRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcEventRepository.class);

    public static final String SQL_EVENTS_NEWERTHAN = "select events.ts, events.message, events.providerId, events.providerUserId, " +
            "connx.displayName, connx.profileUrl, connx.imageUrl, user.username, user.firstName, user.lastName, user.email " +
            "FROM events " +
            "INNER JOIN keemto_user as user ON events.username = user.username " +
            "LEFT JOIN UserConnection as connx ON events.providerId = connx.providerId AND events.providerUserId = connx.providerUserId AND events.username = connx.userId " +
            "WHERE events.ts > ?";

    public static final String SQL_MOSTRECENT_EVENT = "select TOP 1 events.ts, events.message, events.providerId, events.providerUserId, " +
            "connx.displayName, connx.profileUrl, connx.imageUrl, user.username, user.firstName, " +
            "user.lastName, user.email " +
            "from events " +
            "INNER JOIN keemto_user as user ON events.username = user.username " +
            "LEFT JOIN UserConnection as connx ON events.providerId = connx.providerId  " +
            "AND events.provideruserId = connx.providerUserId AND events.username= connx.userId " +
            "where events.username=? AND events.providerId=? ORDER BY ts DESC";

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public JdbcEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> getAllEvents() {
        return getEvents(-1);
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
        try {
            User user = event.getUser();
            ProviderConnection providerConnx = event.getProviderConnection();

            jdbcTemplate.update(insertEvent,
                    new Object[]{event.getTimestamp(), event.getMessage(), user.getUsername(),
                            providerConnx.getProviderId(), providerConnx.getProviderUserId()});

        } catch (DuplicateKeyException e) {
            throw new DuplicateEventException("Unable to persist event " + event +
                    " because another event exists with same eventTime: " + event.getTimestamp(), e);
        }
    }

    @Override
    public Event getMostRecentEvent(AccountKey key) {
        User user = key.getUser();
        String[] parameters = {user.getUsername(), key.getProviderId()};
        try {
            return jdbcTemplate.queryForObject(SQL_MOSTRECENT_EVENT, parameters, new EventRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return createInitializationEvent(user, key.getProviderId());
        }
    }

    private Event createInitializationEvent(User user, String providerId) {
        log.info("User: "
                + user
                + " hasn't event yet for provider: " + providerId
                + ". This is propably the first time application tried to fetch user's connections. An initialization event is returned.");
        //TODO check if null object has to be created into repository or in task
        return new InitializationEvent(user, providerId);
    }

    private final class EventRowMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {

            long timestamp = rs.getLong("ts");
            String message = rs.getString("message");

            User user = new UserRowMapper().mapRow(rs, rowNum);

            ProviderConnection providerConnection = buildProviderConnection(rs);

            return new Event(timestamp, message, user, providerConnection);
        }

        private ProviderConnection buildProviderConnection(ResultSet rs) throws SQLException {
            //TODO create a ProviderConnection factory
            String providerId = rs.getString("providerId");
            String providerUserId = rs.getString("providerUserId");
            String displayName = rs.getString("displayName");
            String profileUrl = rs.getString("profileUrl");
            String imageUrl = rs.getString("imageUrl");
            return new DefaultProviderConnection(providerId, providerUserId, displayName, profileUrl, imageUrl);
        }
    }

}
