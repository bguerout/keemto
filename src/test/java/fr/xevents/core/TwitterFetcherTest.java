package fr.xevents.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterApi;

import com.google.common.collect.Lists;

public class TwitterFetcherTest {

    private TwitterFetcher fetcher;
    private User user;
    private Tweet tweet1;
    private Tweet tweet2;

    private ApiResolver<TwitterApi> apiResolver;
    private TwitterApi api;
    private TimelineOperations timelineOperations;

    @Before
    public void initBeforeTest() throws Exception {

        apiResolver = mock(ApiResolver.class);
        api = mock(TwitterApi.class);
        timelineOperations = mock(TimelineOperations.class);
        fetcher = new TwitterFetcher(apiResolver);
        user = new User("bguerout");
        tweet1 = createTweet("a tweet", user);
        tweet2 = createTweet("a second tweet", user);

        when(apiResolver.getApis(eq(user))).thenReturn(Lists.newArrayList(api));
        when(api.timelineOperations()).thenReturn(timelineOperations);
        when(timelineOperations.getUserTimeline()).thenReturn(Lists.newArrayList(tweet1, tweet2));

    }

    private Tweet createTweet(String message, User tweetOwner) {
        return new Tweet(1, message, new Date(), tweetOwner.getUsername(), "profileImageUrl", new Long(2), 2, "FR",
                "source");
    }

    @Test
    public void shouldFetchEventsUsingUserTimeline() {
        fetcher.fetch(user);

        verify(api).timelineOperations();
        verify(timelineOperations).getUserTimeline();
    }

    @Test
    public void shouldConvertTweetsToEvents() {
        List<Event> events = fetcher.fetch(user);

        assertThat(events.size(), equalTo(2));

        Event event = events.get(0);
        assertThat(event.getTimestamp(), equalTo(tweet1.getCreatedAt().getTime()));
        assertThat(event.getUser(), equalTo("bguerout"));
        assertThat(event.getMessage(), equalTo("a tweet"));

        event = events.get(1);
        assertThat(event.getMessage(), equalTo("a second tweet"));
    }

}
