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

package fr.keemto.core;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/META-INF/spring/applicationContext.xml"})
public class JdbcEventRepositoryIT {

    @Autowired
    private EventRepository repository;
    private ProviderConnection keemtoProviderConnx = new DefaultProviderConnection("keemto");
    private User testUser;
    private AccountKey key;

    @Before
    public void prepare() throws Exception {
        testUser = new User("stnevex", "John", "Doe", "stnevex@gmail.com");
        key = new AccountKey("mail", "stnevex@gmail.com", testUser);
    }

    @Test
    public void shouldReturnANonNullEventsList() throws Exception {

        List<Event> events = repository.getAllEvents();

        assertThat(events.size(), greaterThan(1));
        Event expectedEvent = new Event(1, "hello this is a test", testUser, keemtoProviderConnx);
        assertThat(events, hasItem(expectedEvent));
    }

    @Test
    public void shouldReturnOnlyEventsNewerThan() throws Exception {
        Long newerThan = new Long(1301464284372L);

        List<Event> events = repository.getEvents(newerThan);

        assertThat(events.size(), equalTo(4));
    }


    @Test
    public void shouldReturnMostRecentEvent() {


        Event mostRecentEvent = repository.getMostRecentEvent(key);

        assertThat(mostRecentEvent, notNullValue());
        assertThat(mostRecentEvent.getMessage(), notNullValue());
        assertThat(mostRecentEvent.getUser(), equalTo(testUser));
        assertThat(mostRecentEvent.getTimestamp(), notNullValue());
        assertThat(mostRecentEvent.getProviderConnection(), notNullValue());
        Long lastFetchedTime = new Long(1301464284376L);
        long eventTime = mostRecentEvent.getTimestamp();
        assertThat(eventTime, equalTo(lastFetchedTime));
    }

    @Test
    public void shouldReturnMostRecentEventForAnonymousProvider() {

        Event mostRecentEvent = repository.getMostRecentEvent(key);

        ProviderConnection anonymousProvider = mostRecentEvent.getProviderConnection();
        assertThat(anonymousProvider.isAnonymous(), is(true));
        assertThat(anonymousProvider.getProviderId(), equalTo("mail"));
    }

    @Test
    public void shouldReturnMostRecentEventForSocialProvider() {

        AccountKey twitterAccountKey = new AccountKey("twitter", "@stnevex", testUser);
        Event mostRecentEvent = repository.getMostRecentEvent(twitterAccountKey);

        ProviderConnection socialProvider = mostRecentEvent.getProviderConnection();
        assertThat(socialProvider.getProviderId(), equalTo("twitter"));
        assertThat(socialProvider.getProviderUserId(), equalTo("293724331"));
        assertThat(socialProvider.getDisplayName(), equalTo("@stnevex"));
        assertThat(socialProvider.getProfileUrl(), equalTo("http://twitter.com/stnevex"));
        assertThat(socialProvider.getImageUrl(), equalTo("http://www.gravatar.com/avatar/0a40a289089f2d262cc713c54cae7fa2.png"));
        assertThat(socialProvider.isAnonymous(), is(false));
    }

    @Test
    public void whenUserHasntEventShouldReturnAnInitEvent() {


        AccountKey noFetchedAccountKey = new AccountKey("mail", "stnevex@gmail.com", new User("userWithoutEvents"));
        Event mostRecentEvent = repository.getMostRecentEvent(noFetchedAccountKey);

        assertThat(mostRecentEvent instanceof InitializationEvent, is(true));
        assertThat(mostRecentEvent.getUser().getUsername(), equalTo("userWithoutEvents"));
        assertThat(mostRecentEvent.getTimestamp(), equalTo((long) 0));
    }

    @Test
    public void shouldPersitEvents() throws Exception {

        Event fooEvent = new Event(System.currentTimeMillis(), "foo", testUser, keemtoProviderConnx);
        Event barEvent = new Event(System.currentTimeMillis() + 100, "bar", testUser, keemtoProviderConnx);

        repository.persist(Lists.newArrayList(fooEvent, barEvent));

        List<Event> allEvents = repository.getAllEvents();
        assertThat(allEvents, hasItem(fooEvent));
        assertThat(allEvents, hasItem(barEvent));
    }

    @Test(expected = DuplicateEventException.class)
    public void shouldThrowExWhenTryingToPersist2EventsWithSameTime() throws Exception {
        User owner = new User("owner");
        long eventTime = System.currentTimeMillis();
        Event event = new Event(eventTime, "foo", owner, keemtoProviderConnx);
        Event differentEventWithSameTime = new Event(eventTime, "bar", owner, keemtoProviderConnx);

        repository.persist(Lists.newArrayList(event, differentEventWithSameTime));
    }

}

