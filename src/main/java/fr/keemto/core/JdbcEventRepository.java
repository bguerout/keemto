package fr.keemto.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcEventRepository implements EventRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcEventRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public JdbcEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> getAllEvents() {
        return jdbcTemplate.query("select ts,username,message,providerId from events", new EventRowMapper());
    }

    @Override
    public void persist(List<Event> events) {
        for (Event event : events) {
            persist(event);
        }
    }

    private void persist(Event event) {
        jdbcTemplate.update("insert into events (ts,username,message,providerId) values(?,?,?,?)",
                new Object[] { event.getTimestamp(), event.getUser(), event.getMessage(), event.getProviderId() });
    }

    @Override
    public Event getMostRecentEvent(User user, String providerId) {
        String[] parameters = { user.getUsername(), providerId };
        try {
            return jdbcTemplate
                    .queryForObject(
                            "select TOP 1 ts,username,message,providerId from events where username=? AND providerId=? ORDER BY ts DESC",
                            parameters, new EventRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return createInitializationEvent(user, providerId);
        }
    }

    private Event createInitializationEvent(User user, String providerId) {
        //TODO check if null object has to be created in repository or in task
        log.info("User: "
                + user
                + " hasn't event yet for provider: "+providerId
                + ". This is propably the first time application tried to fetch user's connections. An initialization event is returned.");
        return new InitializationEvent(user.getUsername(), providerId);
    }

    private final class EventRowMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Event(rs.getLong("ts"), rs.getString("username"), rs.getString("message"),
                    rs.getString("providerId"));
        }
    }

}
