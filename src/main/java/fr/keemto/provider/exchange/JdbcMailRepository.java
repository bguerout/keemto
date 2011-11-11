package fr.keemto.provider.exchange;

import fr.keemto.core.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcMailRepository implements MailRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcMailRepository.class);

    public static final String SQL_MAILS_MOST_RECENT = "SELECT TOP 1 ts FROM mail ORDER BY ts DESC";

    public static final String SQL_MAILS_BY_SENDER = "SELECT * FROM mail WHERE sender = ?";

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public JdbcMailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void persist(List<Mail> mails) {
        for (Mail mail : mails) {
            persist(mail);
        }
    }

    private void persist(Mail mail) {
        String insertEvent = "insert into mail (id,sender,subject,body,ts) values(?,?,?,?,?)";
        try {
            jdbcTemplate.update(insertEvent,
                    new Object[]{mail.getId(), mail.getSender(), mail.getSubject(), mail.getBody(), mail.getTimestamp()});

        } catch (DuplicateKeyException e) {
            throw new DuplicateMailException("Unable to persist mail " + mail +
                    " because another event exists with same id: " + mail.getId(), e);
        }
    }

    @Override
    public long getMostRecentMailTime() {
        try {
            return jdbcTemplate.queryForLong(SQL_MAILS_MOST_RECENT);
        } catch (EmptyResultDataAccessException e) {
            log.debug("No mail exists in table.");
            return 0;
        }
    }

    @Override
    public List<Mail> getMailsFrom(String email) {
        return jdbcTemplate.query(SQL_MAILS_BY_SENDER, new String[]{email}, new MailRowMapper());
    }

    private final class MailRowMapper implements RowMapper<Mail> {
        @Override
        public Mail mapRow(ResultSet rs, int rowNum) throws SQLException {

            String id = rs.getString("id");
            String subject = rs.getString("subject");
            String body = rs.getString("body");
            String sender = rs.getString("sender");
            long timestamp = rs.getLong("ts");
            return new Mail(id, sender, subject, body, timestamp);
        }

    }
}
