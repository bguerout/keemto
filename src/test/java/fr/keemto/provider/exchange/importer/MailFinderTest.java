package fr.keemto.provider.exchange.importer;

import com.google.common.collect.Lists;
import fr.keemto.provider.exchange.Mail;
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

        ExchangeServiceFactory factory = mock(ExchangeServiceFactory.class);
        EmailExchangeService emailService = mock(EmailExchangeService.class);
        MailFinder finder = new MailFinder(factory);
        when(factory.createServiceWithTimeSelector(20L)).thenReturn(emailService);

        List<Mail> mails = finder.findEmails(20L);

        verify(factory).createServiceWithTimeSelector(20L);
    }

    @Test
    public void shouldConvertItemToMail() throws Exception {

        Date createdAt = new Date();
        TestingEmailMessage message = new TestingEmailMessage("id", "subject", "body", createdAt, "sender@xebia.fr");
        message.addRecipients("to@xebia.fr");
        message.addRecipients("to2@xebia.fr");
        ExchangeServiceFactory factory = mock(ExchangeServiceFactory.class);
        EmailExchangeService emailService = mock(EmailExchangeService.class);
        when(factory.createServiceWithTimeSelector(20L)).thenReturn(emailService);
        when(emailService.nextElement()).thenReturn(Lists.newArrayList((EmailMessage) message));

        MailFinder finder = new MailFinder(factory);

        List<Mail> mails = finder.findEmails(20L);

        assertThat(mails.size(), is(1));
        Mail mail = mails.get(0);
        assertThat(mail.getId(), equalTo("id"));
        assertThat(mail.getFrom(), equalTo("sender@xebia.fr"));
        assertThat(mail.getSubject(), equalTo("subject"));
        assertThat(mail.getBody(), equalTo("body"));
        assertThat(mail.getRecipients(), hasItems("to@xebia.fr", "to2@xebia.fr"));
        assertThat(mail.getTimestamp(), equalTo(createdAt.getTime()));
    }

    @Test
    public void shouldConvertItemsToMails() throws Exception {

        Date createdAt = new Date();
        EmailMessage message = new TestingEmailMessage("id", "subject", "body", createdAt, "sender@xebia.fr");
        EmailExchangeService emailService = mock(EmailExchangeService.class);
        ExchangeServiceFactory factory = mock(ExchangeServiceFactory.class);
        MailFinder finder = new MailFinder(factory);
        when(factory.createServiceWithTimeSelector(20L)).thenReturn(emailService);
        when(emailService.nextElement()).thenReturn(Lists.newArrayList(message, message));

        List<Mail> mails = finder.findEmails(20L);

        assertThat(mails.size(), is(2));
    }
}
