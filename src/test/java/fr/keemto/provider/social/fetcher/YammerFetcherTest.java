package fr.keemto.provider.social.fetcher;


import com.google.common.collect.Lists;
import fr.keemto.provider.social.EventData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.social.connect.Connection;
import org.springframework.social.yammer.api.MessageOperations;
import org.springframework.social.yammer.api.impl.MessageInfo;
import org.springframework.social.yammer.api.impl.YammerMessage;
import org.springframework.social.yammer.api.impl.YammerTemplate;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YammerFetcherTest {

    private YammerFetcher fetcher;
    private YammerTemplate api;
    private MessageOperations messageOperations;
    private Connection connection;

    @Before
    public void initBeforeTest() throws Exception {

        api = mock(YammerTemplate.class);
        messageOperations = mock(MessageOperations.class);
        fetcher = new YammerFetcher();


        connection = mock(Connection.class);
        when(connection.getApi()).thenReturn(api);
        when(api.messageOperations()).thenReturn(messageOperations);
    }

    @Test
    public void shouldRetrieveAUniqueSentMessage() throws Exception {
        long lastFetchedEventTime = new Date().getTime();
        Date messageCreationDate = new Date(lastFetchedEventTime + 1000);
        YammerMessage message = createYammerMessage("foo", "foo-parsed", messageCreationDate);
        MessageInfo messageInfo = new MessageInfo(Lists.newArrayList(message), null);
        when(messageOperations.getMessagesSent(0, 0, null, 0)).thenReturn(messageInfo);

        List<EventData> datas = fetcher.fetch(connection, lastFetchedEventTime);

        assertThat(datas, notNullValue());
        assertThat(datas.isEmpty(), is(false));
        EventData data = datas.get(0);
        assertThat(data.getMessage(), equalTo("foo"));
        assertThat(data.getTimestamp(), equalTo(messageCreationDate.getTime()));
        assertThat(data.getProviderId(), equalTo("yammer"));
    }

    @Test
    public void shouldRetrieveAllSentMessages() throws Exception {
        long lastFetchedEventTime = new Date().getTime();
        Date messageCreationDate = new Date(lastFetchedEventTime + 1000);
        YammerMessage message = createYammerMessage("foo", "foo-parsed", messageCreationDate);
        YammerMessage message2 = createYammerMessage("bar", "bar-parsed", messageCreationDate);
        MessageInfo messageInfo = new MessageInfo(Lists.newArrayList(message, message2), null);
        when(messageOperations.getMessagesSent(0, 0, null, 0)).thenReturn(messageInfo);

        List<EventData> datas = fetcher.fetch(connection, lastFetchedEventTime);

        assertThat(datas, notNullValue());
        assertThat(datas.size(), equalTo(2));
        assertThat(datas.get(0).getMessage(), equalTo("foo"));
        assertThat(datas.get(1).getMessage(), equalTo("bar"));
    }

    @Test
    public void shouldIgnoreMessagesBeforeLastFetchedEventTime() throws Exception {
        long lastFetchedEventTime = new Date().getTime();
        YammerMessage acceptedMessage = createYammerMessage("accepted", "accepted-parsed", new Date(lastFetchedEventTime + 1000));
        YammerMessage ignoredMessage = createYammerMessage("ignored", "ignored-parsed", new Date(lastFetchedEventTime - 5000));
        MessageInfo messageInfo = new MessageInfo(Lists.newArrayList(acceptedMessage, ignoredMessage), null);
        when(messageOperations.getMessagesSent(0, 0, null, 0)).thenReturn(messageInfo);

        List<EventData> datas = fetcher.fetch(connection, lastFetchedEventTime);

        assertThat(datas, notNullValue());
        assertThat(datas.size(), equalTo(1));
    }


    private YammerMessage createYammerMessage(String message, String messageParsed, Date messageCreationDate) {
        YammerMessage.Body body = new YammerMessage.Body(message, messageParsed, null);
        return new YammerMessage(100, body, null, 0, null, 0, null, null, 0, 0, null, null, false, null, messageCreationDate, false, null, null, 0);
    }
}
