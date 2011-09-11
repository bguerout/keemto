package fr.keemto.core.fetcher.social;


import com.google.common.collect.Lists;
import fr.keemto.core.Event;
import fr.keemto.core.User;
import fr.keemto.core.fetcher.social.ApiResolver;
import fr.keemto.core.fetcher.social.YammerFetcher;
import org.junit.Before;
import org.junit.Test;
import org.springframework.social.yammer.api.MessageOperations;
import org.springframework.social.yammer.api.impl.MessageInfo;
import org.springframework.social.yammer.api.impl.YammerMessage;
import org.springframework.social.yammer.api.impl.YammerTemplate;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YammerFetcherTest {

    private YammerFetcher fetcher;
    private ApiResolver<YammerTemplate> apiResolver;
    private YammerTemplate api;
    private MessageOperations messageOperations;
    private User user;

    @Before
    public void initBeforeTest() throws Exception {

        api = mock(YammerTemplate.class);
        messageOperations = mock(MessageOperations.class);
        apiResolver = mock(ApiResolver.class);
        fetcher = new YammerFetcher(apiResolver);
        user = new User("bguerout");


        when(apiResolver.getApis(eq(user))).thenReturn(Lists.newArrayList(api));
        when(api.messageOperations()).thenReturn(messageOperations);


    }

    @Test
    public void shouldRetrieveAUniqueSentMessage() throws Exception {
        long lastFetchedEventTime = new Date().getTime();
        Date messageCreationDate = new Date(lastFetchedEventTime + 1000);
        YammerMessage message = createYammerMessage("foo", "foo-parsed", messageCreationDate);
        MessageInfo messageInfo = new MessageInfo(Lists.newArrayList(message), null);
        when(messageOperations.getMessagesSent(0, 0, null, 0)).thenReturn(messageInfo);

        List<Event> events = fetcher.fetch(user, lastFetchedEventTime);

        assertThat(events, notNullValue());
        assertThat(events.isEmpty(), is(false));
        Event event = events.get(0);
        assertThat(event.getMessage(), equalTo("foo"));
        assertThat(event.getTimestamp(), equalTo(messageCreationDate.getTime()));
        assertThat(event.getUser(), equalTo(user));
        assertThat(event.getProviderId(), equalTo("yammer"));
    }

    @Test
    public void shouldRetrieveAllSentMessages() throws Exception {
        long lastFetchedEventTime = new Date().getTime();
        Date messageCreationDate = new Date(lastFetchedEventTime + 1000);
        YammerMessage message = createYammerMessage("foo", "foo-parsed", messageCreationDate);
        YammerMessage message2 = createYammerMessage("bar", "bar-parsed", messageCreationDate);
        MessageInfo messageInfo = new MessageInfo(Lists.newArrayList(message, message2), null);
        when(messageOperations.getMessagesSent(0, 0, null, 0)).thenReturn(messageInfo);

        List<Event> events = fetcher.fetch(user, lastFetchedEventTime);

        assertThat(events, notNullValue());
        assertThat(events.size(), equalTo(2));
        assertThat(events.get(0).getMessage(), equalTo("foo"));
        assertThat(events.get(1).getMessage(), equalTo("bar"));
    }

    @Test
    public void shouldIgnoreMessagesBeforeLastFetchedEventTime() throws Exception {
        long lastFetchedEventTime = new Date().getTime();
        YammerMessage acceptedMessage = createYammerMessage("accepted", "accepted-parsed", new Date(lastFetchedEventTime + 1000));
        YammerMessage ignoredMessage = createYammerMessage("ignored", "ignored-parsed", new Date(lastFetchedEventTime - 5000));
        MessageInfo messageInfo = new MessageInfo(Lists.newArrayList(acceptedMessage, ignoredMessage), null);
        when(messageOperations.getMessagesSent(0, 0, null, 0)).thenReturn(messageInfo);

        List<Event> events = fetcher.fetch(user, lastFetchedEventTime);

        assertThat(events, notNullValue());
        assertThat(events.size(), equalTo(1));
    }


    private YammerMessage createYammerMessage(String message, String messageParsed, Date messageCreationDate) {
        YammerMessage.Body body = new YammerMessage.Body(message, messageParsed, null);
        return new YammerMessage(100, body, null, 0, null, 0, null, null, 0, 0, null, null, false, null, messageCreationDate, false, null, null, 0);
    }
}
