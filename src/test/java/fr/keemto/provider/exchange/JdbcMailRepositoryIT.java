package fr.keemto.provider.exchange;

import com.google.common.collect.Lists;
import fr.keemto.config.KeemtoWithSchedulingConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KeemtoWithSchedulingConfig.class, ExchangeConfig.class}, loader = AnnotationConfigContextLoader.class)
public class JdbcMailRepositoryIT {

    @Autowired
    private MailRepository mailRepository;

    @Test
    public void shouldFindMailsBySenderAddress() throws Exception {

        List<Mail> mails = mailRepository.getMailsFrom("stnevex@gmail.com");

        assertThat(mails.size(), equalTo(1));
        Mail mail = mails.get(0);
        assertThat(mail.getId(), equalTo("1"));
        assertThat(mail.getFrom(), equalTo("stnevex@gmail.com"));
        assertThat(mail.getSubject(), equalTo("subject"));
        assertThat(mail.getBody(), equalTo("body"));
        assertThat(mail.getRecipientsAsString(), equalTo("to@xebia.fr,stnevex@xebia.fr"));
        assertThat(mail.getTimestamp(), equalTo(1L));
    }

    @Test
    public void shouldPersistMails() throws Exception {

        List<String> recipients = Lists.newArrayList("to@xebia.fr");
        Mail mail = new Mail("id", "user@gmail.com", "subject", "body", System.currentTimeMillis(), recipients);

        mailRepository.persist(Lists.newArrayList(mail));

        List<Mail> mails = mailRepository.getMailsFrom("user@gmail.com");
        assertThat(mails.size(), equalTo(1));
        assertThat(mails, hasItem(mail));
    }

    @Test
    public void shouldReturnMostRecentMails() throws Exception {

        List<String> recipients = Lists.newArrayList("to@xebia.fr");
        Mail oldMail = new Mail("id21", "stnevex@gmail.com", "subject", "body", 20, recipients);
        long expectedMostRecentTime = System.currentTimeMillis();
        Mail recentMail = new Mail("id22", "stnevex@gmail.com", "subject", "body", expectedMostRecentTime, recipients);
        mailRepository.persist(Lists.newArrayList(oldMail, recentMail));

        long mostRecentMailTime = mailRepository.getMostRecentMailTime();

        assertThat(mostRecentMailTime, equalTo(expectedMostRecentTime));

    }
}
