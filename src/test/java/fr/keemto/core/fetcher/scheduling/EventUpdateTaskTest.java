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

package fr.keemto.core.fetcher.scheduling;

import com.google.common.collect.Lists;
import fr.keemto.core.Event;
import fr.keemto.core.EventRepository;
import fr.keemto.core.User;
import fr.keemto.core.fetcher.Fetcher;
import fr.keemto.core.fetcher.FetchingException;
import fr.keemto.core.fetcher.scheduling.EventUpdateTask;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class EventUpdateTaskTest {

    private EventUpdateTask task;
    private EventRepository eventRepository;
    private Fetcher fetcher;
    private User user;
    private final Event mostRecentEvent = new Event(9999, "user", "message", "provider");

    @Before
    public void initBeforeTest() throws Exception {
        fetcher = mock(Fetcher.class);
        eventRepository = mock(EventRepository.class);
        user = new User("user");
        task = new EventUpdateTask(fetcher, user, eventRepository);

        when(fetcher.getProviderId()).thenReturn("provider");
        when(eventRepository.getMostRecentEvent(any(User.class), eq("provider"))).thenReturn(mostRecentEvent);
    }

    @Test
    public void shouldUseEventRepositoryToObtainLastFetchedEventTime() {

        task.run();

        verify(eventRepository).getMostRecentEvent(user, "provider");
        verify(fetcher).fetch(eq(user), eq(mostRecentEvent.getTimestamp()));
    }

    @Test
    public void shouldPersitFetchedEvents() {
        Event fetchedEvent = new Event(System.currentTimeMillis(), "bguerout", "message", "provider");
        ArrayList<Event> events = Lists.newArrayList(fetchedEvent);
        when(fetcher.fetch(eq(user), anyLong())).thenReturn(events);

        task.run();

        verify(eventRepository).persist(events);
    }

    @Test(expected = FetchingException.class)
    public void whenFetcherFailsShouldThrowException() throws Exception {

        when(fetcher.fetch(eq(user), anyLong())).thenThrow(new RuntimeException());

        task.run();

    }

    @Test(expected = FetchingException.class)
    public void whenEventRepositoryFailsShouldThrowException() throws Exception {

        doThrow(new DataRetrievalFailureException("")).when(eventRepository).persist(anyList());

        task.run();
    }

}
