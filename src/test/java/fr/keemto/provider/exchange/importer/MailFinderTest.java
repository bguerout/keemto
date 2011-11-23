package fr.keemto.provider.exchange.importer;

import com.google.common.collect.Lists;
import fr.keemto.provider.exchange.Email;
import fr.keemto.provider.exchange.TestingEmailMessage;
import microsoft.exchange.webservices.data.EmailMessage;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class MailFinderTest {

    @Test
    public void shouldBuildEmailService() throws Exception {

        ExchangeServiceWrapper serviceWrapper = mock(ExchangeServiceWrapper.class);
        EmailExchangeService emailService = mock(EmailExchangeService.class);
        MailFinder finder = new MailFinder(serviceWrapper);
        when(serviceWrapper.getEmailsNewerThan(20L)).thenReturn(emailService);

        List<Email> emails = finder.findEmails(20L);

        verify(serviceWrapper).getEmailsNewerThan(20L);
    }

    @Test
    public void shouldConvertItemToMail() throws Exception {

        Date createdAt = new Date();
        TestingEmailMessage message = new TestingEmailMessage("id", "subject", "body", createdAt, "sender@xebia.fr");
        message.addRecipients("to@xebia.fr");
        message.addRecipients("to2@xebia.fr");
        ExchangeServiceWrapper serviceWrapper = mock(ExchangeServiceWrapper.class);
        EmailExchangeService emailService = mock(EmailExchangeService.class);
        when(serviceWrapper.getEmailsNewerThan(20L)).thenReturn(emailService);
        when(emailService.hasMoreElements()).thenReturn(true).thenReturn(false);
        when(emailService.nextElement()).thenReturn(Lists.newArrayList((EmailMessage) message));

        MailFinder finder = new MailFinder(serviceWrapper);

        List<Email> emails = finder.findEmails(20L);

        assertThat(emails.size(), is(1));
        Email email = emails.get(0);
        assertThat(email.getId(), equalTo("id"));
        assertThat(email.getFrom(), equalTo("sender@xebia.fr"));
        assertThat(email.getSubject(), equalTo("subject"));
        assertThat(email.getBody(), equalTo("body"));
        assertThat(email.getRecipients(), hasItems("to@xebia.fr", "to2@xebia.fr"));
        assertThat(email.getTimestamp(), equalTo(createdAt.getTime()));
    }

    @Test
    public void shouldConvertItemsToMails() throws Exception {

        Date createdAt = new Date();
        EmailMessage message = new TestingEmailMessage("id", "subject", "body", createdAt, "sender@xebia.fr");
        ExchangeServiceWrapper serviceWrapper = mock(ExchangeServiceWrapper.class);
        MailFinder finder = new MailFinder(serviceWrapper);
        EmailExchangeService emailService = mock(EmailExchangeService.class);
        when(serviceWrapper.getEmailsNewerThan(20L)).thenReturn(emailService);
        when(emailService.hasMoreElements()).thenReturn(true).thenReturn(false);
        when(emailService.nextElement()).thenReturn(Lists.newArrayList(message, message));

        List<Email> emails = finder.findEmails(20L);

        assertThat(emails.size(), is(2));
    }
}
