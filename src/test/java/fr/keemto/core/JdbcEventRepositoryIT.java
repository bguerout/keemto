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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/META-INF/spring/applicationContext.xml"})
public class JdbcEventRepositoryIT {

    @Autowired
    private EventRepository repository;
    private ProviderConnection keemtoProviderConnx = new DefaultProviderConnection("keemto");
    private User testUser = new User("stnevex");


    @Test
    public void shouldReturnANonNullEventsList() throws Exception {
        List<Event> events = repository.getAllEvents();

        assertThat(events, notNullValue());
    }

    @Test
    public void whenEventsExistShouldReturnAllEvents() throws Exception {

        List<Event> events = repository.getAllEvents();

        Event expectedEvent = new Event(1, "hello this is a test", testUser, keemtoProviderConnx);
        assertThat(events, hasItem(expectedEvent));
    }

    @Test
    public void shouldReturnMostRecentEventForMail() {

        Event mostRecentEvent = repository.getMostRecentEvent(testUser, "mail");

        assertThat(mostRecentEvent, notNullValue());
        assertThat(mostRecentEvent.getUser(), equalTo(testUser));
        assertThat(mostRecentEvent.getTimestamp(), equalTo(new Long(1301464284376L)));
        ProviderConnection providerConnection = mostRecentEvent.getProviderConnection();
        assertThat(providerConnection.getProviderId(), equalTo("mail"));
        assertThat(providerConnection.isAnonymous(), is(true));
    }

    @Test
    public void shouldReturnMostRecentEventForTwitter() {

        Event mostRecentEvent = repository.getMostRecentEvent(testUser, "twitter");

        ProviderConnection providerConnection = mostRecentEvent.getProviderConnection();
        assertThat(providerConnection.getProviderId(), equalTo("twitter"));
        assertThat(providerConnection.getProviderUserId(), equalTo("293724331"));
        assertThat(providerConnection.getDisplayName(), equalTo("@stnevex"));
        assertThat(providerConnection.getProfileUrl(), equalTo("http://twitter.com/stnevex"));
        assertThat(providerConnection.getImageUrl(), equalTo("http://a0.twimg.com/sticky/default_profile_images/default_profile_5_normal.png"));
        assertThat(providerConnection.isAnonymous(), is(false));
    }

    @Test
    public void whenUserHasntEventShouldReturnAnInitEvent() {
        User userWithoutEvents = new User("userWithoutEvents");

        Event mostRecentEvent = repository.getMostRecentEvent(userWithoutEvents, "mail");

        assertThat(mostRecentEvent, notNullValue());
        assertThat(mostRecentEvent instanceof InitializationEvent, is(true));
        assertThat(mostRecentEvent.getUser(), equalTo(userWithoutEvents));
        assertThat(mostRecentEvent.getTimestamp(), equalTo((long) 0));
        ProviderConnection initialisationProviderConnection = mostRecentEvent.getProviderConnection();
        assertThat(initialisationProviderConnection.getProviderId(), equalTo("mail"));
        assertThat(initialisationProviderConnection.isAnonymous(), is(true));
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

