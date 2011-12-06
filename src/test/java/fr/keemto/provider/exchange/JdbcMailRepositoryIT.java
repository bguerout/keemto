package fr.keemto.provider.exchange;

import com.google.common.collect.Lists;
import fr.keemto.config.CoreConfig;
import fr.keemto.config.ProviderConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, ProviderConfig.class}, loader = AnnotationConfigContextLoader.class)
public class JdbcMailRepositoryIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MailRepository mailRepository;

    @Before
    public void setUp() throws Exception {
        mailRepository = new JdbcMailRepository(jdbcTemplate);
    }

    @Test
    public void shouldFindMailsBySenderAddress() throws Exception {

        List<Email> emails = mailRepository.getMails("stnevex@gmail.com", 0);

        assertThat(emails.size(), equalTo(2));
        Email email = emails.get(0);
        assertThat(email.getId(), equalTo("1"));
        assertThat(email.getFrom(), equalTo("stnevex@gmail.com"));
        assertThat(email.getSubject(), equalTo("subject"));
        assertThat(email.getBody(), equalTo("body"));
        assertThat(email.getRecipientsAsString(), equalTo("to@xebia.fr,stnevex@xebia.fr"));
        assertThat(email.getTimestamp(), equalTo(1322076312000L));
    }


    @Test
    public void shouldFindMailsNewerThanATime() throws Exception {

        List<Email> emails = mailRepository.getMails("stnevex@gmail.com", 100L);

        assertThat(emails.size(), equalTo(2));
    }

    @Test
    public void shouldPersistMails() throws Exception {

        List<String> recipients = Lists.newArrayList("to@xebia.fr");
        Email email = new Email("id", "user@gmail.com", "subject", "body", System.currentTimeMillis(), recipients);

        mailRepository.persist(Lists.newArrayList(email));

        List<Email> emails = mailRepository.getMails("user@gmail.com", 0);
        assertThat(emails.size(), equalTo(1));
        assertThat(emails, hasItem(email));
    }

    @Test
    public void shouldReturnMostRecentMails() throws Exception {

        List<String> recipients = Lists.newArrayList("to@xebia.fr");
        Email oldEmail = new Email("id21", "stnevex@gmail.com", "subject", "body", 20, recipients);
        long expectedMostRecentTime = System.currentTimeMillis();
        Email recentEmail = new Email("id22", "stnevex@gmail.com", "subject", "body", expectedMostRecentTime, recipients);
        mailRepository.persist(Lists.newArrayList(oldEmail, recentEmail));

        long mostRecentMailTime = mailRepository.getMostRecentMailCreationTime();

        assertThat(mostRecentMailTime, equalTo(expectedMostRecentTime));

    }
}
