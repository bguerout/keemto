package fr.keemto.provider.exchange;

import com.google.common.collect.Lists;
import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.Event;
import fr.keemto.core.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class ExchangeAccountTest {

    private MailRepository mailRepository;
    private ExchangeAccount account;
    private AccountKey key;

    @Before
    public void setUp() throws Exception {

        User user = new User("stnevex", "Ben", "G", "stnevex@gmail.com");
        key = new AccountKey("exchange", user.getEmail(), user);
        mailRepository = mock(MailRepository.class);
        account = new ExchangeAccount(key, mailRepository);
    }

    @Test
    public void shouldFetchEventsFromMailRepository() throws Exception {

        account.fetch(22L);

        verify(mailRepository).getMails("stnevex@gmail.com", 22L);
    }

    @Test
    public void shouldConvertMailsToEvents() throws Exception {

        List<String> recipients = Lists.newArrayList("1@domain.fr", "2@domain.fr");
        long createdAt = System.currentTimeMillis();
        Email email = new Email("id", "user@gmail.com", "subject", "body", createdAt, recipients);
        when(mailRepository.getMails("stnevex@gmail.com", 22L)).thenReturn(Lists.newArrayList(email));

        List<Event> events = account.fetch(22L);

        assertThat(events, notNullValue());
        assertThat(events.size(), equalTo(1));
        Event event = events.get(0);
        assertThat(event.getTimestamp(), equalTo(createdAt));
        assertThat(event.getMessage(), equalTo("<pre>body</pre>"));
        assertThat(event.getAccount(), equalTo((Account) account));

    }

    @Test
    public void shouldIgnoredNonAllowedRecipients() throws Exception {

        long createdAt = System.currentTimeMillis();
        Email email = new Email("id", "user@gmail.com", "subject", "body", createdAt, Lists.newArrayList("allowed"));
        Email rejectedEmail = new Email("id", "user@gmail.com", "subject", "body", createdAt, Lists.newArrayList("denied"));
        when(mailRepository.getMails("stnevex@gmail.com", 22L)).thenReturn(Lists.newArrayList(email, rejectedEmail));

        ExchangeAccount accountWithRecipients = new ExchangeAccount(key, Lists.newArrayList("allowed"), mailRepository);

        List<Event> events = accountWithRecipients.fetch(22L);

        assertThat(events.size(), equalTo(1));
    }


    @Test
    public void shouldUseEmailAsDisplayName() throws Exception {

        assertThat(account.getDisplayName(), equalTo("stnevex@gmail.com"));
    }
}
