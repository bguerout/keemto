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

import fr.keemto.core.EventRepository;
import fr.keemto.core.User;
import fr.keemto.core.fetcher.Fetcher;
import fr.keemto.core.fetcher.FetcherLocator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FetchingTaskFactoryTest {

    private FetchingTaskFactory fetchingTaskFactory;
    private EventRepository eventRepository;
    private Fetcher fetcher;
    private List<Fetcher> fetchers;
    private final User user = new User("user");

    @Before
    public void initBeforeTest() throws Exception {

        fetchers = new ArrayList<Fetcher>();
        fetcher = mock(Fetcher.class);
        fetchers.add(fetcher);
        FetcherLocator fetcherLocator = mock(FetcherLocator.class);
        when(fetcherLocator.getFetchersFor(user)).thenReturn(fetchers);

        eventRepository = mock(EventRepository.class);
        fetchingTaskFactory = new FetchingTaskFactory(eventRepository, fetcherLocator);

    }

    @Test
    public void shouldCreateTaskWithUser() throws Exception {

        List<FetchingTask> tasks = fetchingTaskFactory.createTasks(user);

        assertThat(tasks, notNullValue());
        assertThat(tasks.size(), equalTo(1));
    }

    @Test
    public void shouldCreateTasksWithUser() throws Exception {

        Fetcher fetcher2 = mock(Fetcher.class);
        when(fetcher2.canFetch(any(User.class))).thenReturn(true);
        fetchers.add(fetcher2);

        List<FetchingTask> tasks = fetchingTaskFactory.createTasks(user);

        assertThat(tasks, notNullValue());
        assertThat(tasks.size(), equalTo(2));
    }

}