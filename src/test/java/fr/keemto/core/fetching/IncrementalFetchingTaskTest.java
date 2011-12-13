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

package fr.keemto.core.fetching;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.Event;
import fr.keemto.core.EventRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class IncrementalFetchingTaskTest {

    private FetchingTask task;
    private EventRepository eventRepository;
    private Event mostRecentEvent;
    private Account account;

    @Before
    public void initBeforeTest() throws Exception {
        eventRepository = mock(EventRepository.class);
        account = mock(Account.class);
        mostRecentEvent = new Event(9999, "message", mock(Account.class));
        task = new IncrementalFetchingTask(account, eventRepository);
    }

    @Test
    public void shouldPersitFetchedEvents() {

        ArrayList<Event> events = new ArrayList<Event>();
        when(eventRepository.getMostRecentEvent(any(Account.class))).thenReturn(mostRecentEvent);
        when(account.fetch(anyLong())).thenReturn(events);

        task.run();

        verify(eventRepository).persist(events);
    }

    @Test(expected = FetchingException.class)
    public void whenFetcherFailsShouldThrowException() throws Exception {

        when(eventRepository.getMostRecentEvent(any(Account.class))).thenReturn(mostRecentEvent);
        when(account.fetch(anyLong())).thenThrow(new RuntimeException());

        task.run();
    }

    @Test(expected = FetchingException.class)
    public void whenEventRepositoryFailsShouldThrowException() throws Exception {

        when(eventRepository.getMostRecentEvent(any(Account.class))).thenReturn(mostRecentEvent);
        doThrow(new DataRetrievalFailureException("")).when(eventRepository).persist(anyList());

        task.run();
    }

    @Test
    public void shouldUseAccountKeyHashCodeToCreateTaskId() throws Exception {

        AccountKey key = mock(AccountKey.class);
        when(account.getKey()).thenReturn(key);

        String taskId = task.getTaskId();

        assertThat(taskId, equalTo(""+key.hashCode()));
    }
}
