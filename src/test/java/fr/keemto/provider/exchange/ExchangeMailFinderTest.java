package fr.keemto.provider.exchange;

import com.google.common.collect.Lists;
import microsoft.exchange.webservices.data.EmailMessage;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

public class ExchangeMailFinderTest {

    @Test
    public void shouldBuildEmailService() throws Exception {

        ExchangeServiceFactory factory = mock(ExchangeServiceFactory.class);
        EmailExchangeService emailService = mock(EmailExchangeService.class);
        ExchangeMailFinder finder = new ExchangeMailFinder(factory);
        when(factory.createEmailExchange(20L)).thenReturn(emailService);

        List<Mail> mails = finder.findEmails(20L);

        verify(factory).createEmailExchange(20L);
    }

    @Test
    public void shouldConvertItemToMail() throws Exception {

        Date createdAt = new Date();
        EmailMessage message = new TestingEmailMessage("id", "subject", "body", createdAt, "sender@xebia.fr");
        ExchangeServiceFactory factory = mock(ExchangeServiceFactory.class);
        EmailExchangeService emailService = mock(EmailExchangeService.class);
        when(factory.createEmailExchange(20L)).thenReturn(emailService);
        when(emailService.nextElement()).thenReturn(Lists.newArrayList(message));

        ExchangeMailFinder finder = new ExchangeMailFinder(factory);

        List<Mail> mails = finder.findEmails(20L);

        assertThat(mails.size(), is(1));
        Mail mail = mails.get(0);
        assertThat(mail.getId(), equalTo("id"));
        assertThat(mail.getSender(), equalTo("sender@xebia.fr"));
        assertThat(mail.getSubject(), equalTo("subject"));
        assertThat(mail.getBody(), equalTo("body"));
        assertThat(mail.getTimestamp(), equalTo(createdAt.getTime()));
    }

    @Test
    public void shouldConvertItemsToMails() throws Exception {

        Date createdAt = new Date();
        EmailMessage message = new TestingEmailMessage("id", "subject", "body", createdAt, "sender@xebia.fr");
        EmailExchangeService emailService = mock(EmailExchangeService.class);
        ExchangeServiceFactory factory = mock(ExchangeServiceFactory.class);
        ExchangeMailFinder finder = new ExchangeMailFinder(factory);
        when(factory.createEmailExchange(20L)).thenReturn(emailService);
        when(emailService.nextElement()).thenReturn(Lists.newArrayList(message, message));

        List<Mail> mails = finder.findEmails(20L);

        assertThat(mails.size(), is(2));
    }
}
