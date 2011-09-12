/*
 * Copyright (C) 2010 Benoit Guerout <bguerout at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.keemto.core.fetcher.social;

import com.google.common.collect.Lists;
import fr.keemto.core.Event;
import fr.keemto.core.User;
import fr.keemto.core.fetcher.social.ApiResolver;
import fr.keemto.core.fetcher.social.TwitterFetcher;
import org.junit.Before;
import org.junit.Test;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class TwitterFetcherTest {

    private TwitterFetcher fetcher;
    private Tweet tweet1;
    private Tweet tweet2;
    private User user;
    private long since;
    private ApiResolver<Twitter> apiResolver;
    private Twitter api;
    private TimelineOperations timelineOperations;

    @Before
    public void initBeforeTest() throws Exception {

        apiResolver = mock(ApiResolver.class);
        api = mock(Twitter.class);
        timelineOperations = mock(TimelineOperations.class);
        fetcher = new TwitterFetcher(apiResolver);
        user = new User("bguerout");
        tweet1 = createTweet("a tweet", System.currentTimeMillis());
        tweet2 = createTweet("a second tweet", System.currentTimeMillis() + 10);
        since = 0;

        when(apiResolver.getApis(eq(user))).thenReturn(Lists.newArrayList(api));
        when(api.timelineOperations()).thenReturn(timelineOperations);
        when(timelineOperations.getUserTimeline()).thenReturn(Lists.newArrayList(tweet1, tweet2));

    }

    private Tweet createTweet(String message, long creationDate) {
        return new Tweet(1, message, new Date(creationDate), "mypseudo", "profileImageUrl", new Long(2),
                2, "FR", "source");
    }

    @Test
    public void shouldFetchEventsUsingUserTimeline() {
        fetcher.fetch(user, since);

        verify(api).timelineOperations();
        verify(timelineOperations).getUserTimeline();
    }

    @Test
    public void shouldConvertTweetsToEvents() {
        List<Event> events = fetcher.fetch(user, since);

        assertThat(events.size(), equalTo(2));

        Event event = events.get(0);
        assertThat(event.getTimestamp(), equalTo(tweet1.getCreatedAt().getTime()));
        assertThat(event.getUser(), equalTo(user));
        assertThat(event.getMessage(), equalTo("a tweet"));
        assertThat(event.getProviderId(), equalTo("twitter"));

        event = events.get(1);
        assertThat(event.getMessage(), equalTo("a second tweet"));
    }

    @Test
    public void shouldIgnoreTweetsBeforeLastFetchedEventTime() {

        long tweet1CreationTime = tweet1.getCreatedAt().getTime();

        List<Event> events = fetcher.fetch(user, tweet1CreationTime);

        assertThat(events.size(), greaterThan(0));
        for (Event event : events) {
            assertThat(event.getTimestamp(), greaterThan(tweet1CreationTime));
        }
    }

}
