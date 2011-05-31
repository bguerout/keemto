package fr.xevents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;

import com.google.common.collect.Lists;

public class TwitterFetcherTest {

    private TwitterFetcher fetcher;
    private List<User> users;
    private Tweet tweet1;
    private Tweet tweet2;

    @Mock
    private TimelineOperations api;

    @Mock
    private ApiResolver<TimelineOperations> apiResolver;
    private User user;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        fetcher = new TwitterFetcher(apiResolver);
        user = new User("bguerout");
        users = Lists.newArrayList(user);
        tweet1 = new Tweet(1, "a tweet", new Date(), "bguerout", "profileImageUrl", new Long(2), 2, "FR", "source");
        tweet2 = new Tweet(2, "a second tweet", new Date(), "bguerout", "profileImageUrl", new Long(2), 2, "FR",
                "source");

        when(apiResolver.getApis(eq(new User("bguerout")))).thenReturn(Lists.newArrayList(api));
        when(api.getUserTimeline()).thenReturn(Lists.newArrayList(tweet1, tweet2));

    }

    @Test
    public void shouldFetchANonNullEventsList() {
        List<Event> events = fetcher.fetch(users);

        assertThat(events, notNullValue());
    }

    @Test
    public void shouldFetchEventsForUser() {

        List<Event> events = fetcher.fetch(users);

        assertThat(events.size(), greaterThan(0));
        Event event = events.get(0);
        assertThat(event.getUser(), equalTo("bguerout"));
        assertThat(event.getMessage(), equalTo("a tweet"));
    }

    @Test
    public void shouldFetchEventsWithApiResolver() {
        fetcher.fetch(users);

        verify(api).getUserTimeline();
    }

    @Test
    public void shouldConvertTweetsToEvents() {

        List<Event> events = fetcher.fetch(users);

        assertThat(events.size(), equalTo(2));
        Event event = events.get(0);
        assertThat(event.getTimestamp(), equalTo(tweet1.getCreatedAt().getTime()));
        assertThat(event.getUser(), equalTo(tweet1.getFromUser()));
        assertThat(event.getMessage(), equalTo(tweet1.getText()));
    }

    @Test
    public void shouldHandleMultipleApis() {

        ArrayList<TimelineOperations> multipleApis = Lists.newArrayList(api, api);
        when(apiResolver.getApis(eq(user))).thenReturn(multipleApis);

        List<Event> events = fetcher.fetch(users);

        assertThat(events.size(), equalTo(4));
    }
}
