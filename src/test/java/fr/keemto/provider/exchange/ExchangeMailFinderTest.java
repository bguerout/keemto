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
    public void shouldObtainDataFromExchangeService() throws Exception {

        ExchangeServiceWrapper wrapper = mock(ExchangeServiceWrapper.class);
        ExchangeMailFinder finder = new ExchangeMailFinder(wrapper);

        List<Mail> mails = finder.fetch(20L);

        verify(wrapper).getItems(20L);
    }

    @Test
    public void shouldConvertItemToMail() throws Exception {

        ExchangeServiceWrapper wrapper = mock(ExchangeServiceWrapper.class);
        ExchangeMailFinder finder = new ExchangeMailFinder(wrapper);
        Date createdAt = new Date();
        EmailMessage message = new TestingEmailMessage("id", "subject", "body", createdAt, "sender@xebia.fr");
        when(wrapper.getItems(20L)).thenReturn(Lists.newArrayList(message));

        List<Mail> mails = finder.fetch(20L);

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

        ExchangeServiceWrapper wrapper = mock(ExchangeServiceWrapper.class);
        ExchangeMailFinder finder = new ExchangeMailFinder(wrapper);
        Date createdAt = new Date();
        EmailMessage message = new TestingEmailMessage("id", "subject", "body", createdAt, "sender@xebia.fr");
        when(wrapper.getItems(20L)).thenReturn(Lists.newArrayList(message, message));

        List<Mail> mails = finder.fetch(20L);

        assertThat(mails.size(), is(2));
    }
}
