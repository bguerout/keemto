package fr.keemto.provider.exchange;

import com.google.common.collect.Lists;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/META-INF/spring/core-config.xml"})
public class JdbcMailRepositoryIT {

    @Autowired
    private MailRepository mailRepository;

    @Before
    public void prepare() throws Exception {
    }

    @Test
    public void shouldFindMailsBySenderAddress() throws Exception {

        List<Mail> mails = mailRepository.getMailsFrom("stnevex@gmail.com");

        assertThat(mails.size(), equalTo(1));
        Mail mail = mails.get(0);
        assertThat(mail.getId(), CoreMatchers.equalTo("1"));
        assertThat(mail.getSender(), CoreMatchers.equalTo("stnevex@gmail.com"));
        assertThat(mail.getSubject(), CoreMatchers.equalTo("subject"));
        assertThat(mail.getBody(), CoreMatchers.equalTo("body"));
        assertThat(mail.getTimestamp(), CoreMatchers.equalTo(1L));
    }

    @Test
    public void shouldPersistMails() throws Exception {

        Mail mail = new Mail("id", "user@gmail.com", "subject", "body", System.currentTimeMillis());

        mailRepository.persist(Lists.newArrayList(mail));

        List<Mail> mails = mailRepository.getMailsFrom("user@gmail.com");
        assertThat(mails.size(), equalTo(1));
        assertThat(mails, hasItem(mail));
    }

    @Test
    public void shouldReturnMostRecentMails() throws Exception {

        Mail oldMail = new Mail("id21", "stnevex@gmail.com", "subject", "body", 20);
        long expectedMostRecentTime = System.currentTimeMillis();
        Mail recentMail = new Mail("id22", "stnevex@gmail.com", "subject", "body", expectedMostRecentTime);
        mailRepository.persist(Lists.newArrayList(oldMail, recentMail));

        long mostRecentMailTime = mailRepository.getMostRecentMailTime();

        assertThat(mostRecentMailTime, equalTo(expectedMostRecentTime));

    }
}
